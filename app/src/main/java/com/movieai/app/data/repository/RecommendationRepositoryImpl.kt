package com.movieai.app.data.repository

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
                val parsed = json.decodeFromString<RecommendationsWrapper>(raw)
                val enriched = coroutineScope {
                    parsed.recommendations.mapIndexed { idx, item ->
                        async { enrich(idx + 1, item) }
                    }.awaitAll()
                }
                recommendationDao.replaceAll(enriched.map { it.toEntity() })
                enriched
            }
        }

    private suspend fun enrich(rank: Int, item: RecommendationsWrapper.RecItem): Recommendation {
        val tmdbHit = runCatching {
            tmdb.searchMovies(query = item.title).results.firstOrNull()
        }.getOrNull()
        val movie = tmdbHit?.movieDtoToDomain()?.copy(genres = item.genres)
            ?: fallbackMovie(item)
        return Recommendation(rank = rank, movie = movie, reason = item.reason)
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
            각 추천에는 사용자에게 호소력 있는 2-3문장 추천 이유를 포함하세요.

            다음 JSON 형식으로만 응답하세요:
            {"recommendations":[{"title":"","year":2024,"genres":["",""],"reason":""}]}
        """.trimIndent()
    }

    private fun fallbackMovie(item: RecommendationsWrapper.RecItem): Movie = Movie(
        id            = item.title.hashCode().toLong(),
        title         = item.title,
        originalTitle = item.title,
        year          = item.year,
        posterUrl     = null,
        backdropUrl   = null,
        rating        = 0.0,
        genres        = item.genres,
    )
}
