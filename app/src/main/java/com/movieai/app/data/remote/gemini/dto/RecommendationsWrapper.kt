package com.movieai.app.data.remote.gemini.dto

import kotlinx.serialization.Serializable

/**
 * Parsed Gemini JSON payload. Matches the exact response shape demanded
 * by our prompt: `{"recommendations":[{title, year, genres, reason}]}`.
 */
@Serializable
data class RecommendationsWrapper(
    val recommendations: List<RecItem> = emptyList(),
) {
    @Serializable
    data class RecItem(
        val title: String,
        val year: Int? = null,
        val genres: List<String> = emptyList(),
        val reason: String,
    )
}
