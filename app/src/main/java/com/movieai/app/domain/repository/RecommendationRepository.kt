package com.movieai.app.domain.repository

import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.Recommendation
import kotlinx.coroutines.flow.Flow

/**
 * Contract for AI-driven recommendations.
 *
 * Caching strategy: results are persisted in Room. The Recommend screen
 * observes the cache via [observeRecommendations] and triggers a fresh
 * Gemini call via [refresh] on pull-to-refresh or when the cache is
 * empty and the user has enough favorites.
 */
interface RecommendationRepository {

    /** Live recommendation list (empty if never fetched). */
    fun observeRecommendations(): Flow<List<Recommendation>>

    /**
     * Call Gemini with the current [favorites], enrich results via TMDB,
     * and atomically replace the cache. Returns the new list on success.
     */
    suspend fun refresh(favorites: List<Movie>): Result<List<Recommendation>>
}
