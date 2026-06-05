package com.movieai.app.data.repository

import android.util.Log
import com.movieai.app.BuildConfig
import com.movieai.app.data.local.RecommendationDao
import com.movieai.app.data.mapper.toDomain
import com.movieai.app.data.mapper.toEntity
import com.movieai.app.data.remote.gemini.GeminiApi
import com.movieai.app.data.remote.gemini.dto.GeminiRequestDto
import com.movieai.app.data.remote.gemini.dto.RecommendationsWrapper
import com.movieai.app.data.remote.tmdb.TmdbApi
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.Recommendation
import com.movieai.app.domain.repository.RecommendationRepository
import com.movieai.app.data.mapper.toDomain as movieDtoToDomain
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MovieAi/Recommend"

@Singleton
class RecommendationRepositoryImpl @Inject constructor(
    private val gemini: GeminiApi,
    private val tmdb: TmdbApi,
    private val recommendationDao: RecommendationDao,
    private val json: Json,
) : RecommendationRepository {

    override fun observeRecommendations(): Flow<List<Recommendation>> =
        recommendationDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refresh(favorites: List<Movie>): Result<List<Recommendation>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val body = GeminiRequestDto(
                    contents = listOf(
                        GeminiRequestDto.Content(parts = listOf(GeminiRequestDto.Part(buildPrompt(favorites))))
                    ),
                    generationConfig = GeminiRequestDto.GenerationConfig(),
                )
                val resp = gemini.generate(BuildConfig.GEMINI_API_KEY, body)
                val raw = resp.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: error("Empty Gemini response")
                Log.d(TAG, "Gemini raw response (${raw.length} chars):\n$raw")
                val cleaned = stripCodeFences(raw)
                val parsed = try {
                    json.decodeFromString<RecommendationsWrapper>(cleaned)
                } catch (e: Throwable) {
                    Log.e(TAG, "JSON parse failed. Cleaned was:\n$cleaned", e)
                    throw e
                }
                val outcomes = coroutineScope {
                    parsed.recommendations.map { item ->
                        async { enrich(item) }
                    }.awaitAll()
                }
                val enriched = outcomes
                    .filterIsInstance<Enriched.Hit>()
                    .map { it.recommendation }
                    .distinctBy { it.movie.id } // collapse duplicate TMDB matches
                    .mapIndexed { idx, rec -> rec.copy(rank = idx + 1) }
                if (enriched.isEmpty()) {
                    // Distinguish a real TMDB outage from a genuine zero-match so
                    // the user isn't told "no movies found" when the network failed.
                    if (outcomes.any { it is Enriched.Failed }) {
                        error("영화 정보를 불러오지 못했어요. 잠시 후 다시 시도해주세요.")
                    } else {
                        error("추천할 영화를 찾지 못했어요. 다시 시도해주세요.")
                    }
                }
                recommendationDao.replaceAll(enriched.map { it.toEntity() })
                enriched
            }
        }

    /**
     * Looks the recommended title up on TMDB, distinguishing three outcomes:
     * - [Enriched.Hit] — a real, navigable movie was found.
     * - [Enriched.Miss] — the API responded but had no match; the recommendation
     *   is dropped (a synthetic id would 404 on the detail screen and show no
     *   poster).
     * - [Enriched.Failed] — the lookup itself errored (timeout, 5xx, …). Kept
     *   distinct from a miss so the caller can report a network error instead of
     *   a misleading "not found".
     *
     * Rank here is a placeholder, reassigned by the caller after dedup/filtering.
     */
    private suspend fun enrich(item: RecommendationsWrapper.RecItem): Enriched {
        val results = try {
            tmdb.searchMovies(query = item.title).results
        } catch (e: CancellationException) {
            throw e // never swallow cancellation — preserve structured concurrency
        } catch (e: Throwable) {
            Log.w(TAG, "TMDB enrich failed for '${item.title}'", e)
            return Enriched.Failed
        }
        val hit = results.firstOrNull() ?: return Enriched.Miss
        val movie = hit.movieDtoToDomain().copy(genres = item.genres)
        return Enriched.Hit(Recommendation(rank = 0, movie = movie, reason = item.reason))
    }

    private sealed interface Enriched {
        data class Hit(val recommendation: Recommendation) : Enriched
        data object Miss : Enriched
        data object Failed : Enriched
    }

    private fun buildPrompt(favs: List<Movie>): String {
        val list = favs.joinToString("\n") { fav ->
            "- ${fav.title} (${fav.year ?: "?"}) · ${fav.genres.joinToString(",")} · ★${"%.1f".format(fav.rating)}"
        }
        return """
            사용자가 즐겨찾기한 영화 목록:
            $list

            이 사람의 취향을 분석해 비슷한 결의 영화 5편을 추천해주세요.
            추천 작품은 이미 좋아한 영화와 중복되지 않아야 합니다.
            각 추천에는 사용자에게 호소력 있는 한 문장(60자 이내) 추천 이유를 포함하세요.

            다음 JSON 형식으로만 응답하세요:
            {"recommendations":[{"title":"","year":2024,"genres":["",""],"reason":""}]}
        """.trimIndent()
    }

    /**
     * Gemini occasionally wraps JSON in ```json ... ``` fences even when
     * `responseMimeType: application/json` is set — known 2.5-flash quirk
     * for Korean + complex JSON output. Strip leading/trailing fences so
     * parsing stays robust regardless.
     */
    private fun stripCodeFences(raw: String): String {
        val trimmed = raw.trim()
        if (!trimmed.startsWith("```")) return trimmed
        return trimmed
            .removePrefix("```json")
            .removePrefix("```JSON")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

}
