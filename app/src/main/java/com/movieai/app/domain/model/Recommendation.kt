package com.movieai.app.domain.model

/**
 * A single AI-generated recommendation card on the Recommend screen.
 *
 * The [movie] field is enriched by hitting TMDB after Gemini returns
 * titles — so by the time a `Recommendation` reaches the UI, the poster
 * and rating are already populated whenever TMDB had a match.
 *
 * @property rank 1-based ranking (#01..#05) as shown in the design.
 * @property movie Enriched movie summary (poster, rating). Falls back to
 *           a title-only stub when TMDB has no match.
 * @property reason 2–3 sentence rationale from Gemini explaining why
 *           this movie fits the user's favorites.
 */
data class Recommendation(
    val rank: Int,
    val movie: Movie,
    val reason: String,
)
