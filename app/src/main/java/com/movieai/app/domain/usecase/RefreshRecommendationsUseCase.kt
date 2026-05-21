package com.movieai.app.domain.usecase

import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.Recommendation
import com.movieai.app.domain.repository.RecommendationRepository
import javax.inject.Inject

/**
 * Trigger a fresh Gemini call using the supplied [favorites] snapshot.
 *
 * The caller (RecommendViewModel) is responsible for the "favorites ≥ 3"
 * gate — by the time this use case is invoked, the snapshot is assumed
 * to be valid. Returns [Result.failure] on network / parse errors so
 * the ViewModel can surface a user-friendly message.
 */
class RefreshRecommendationsUseCase @Inject constructor(
    private val repository: RecommendationRepository,
) {
    suspend operator fun invoke(favorites: List<Movie>): Result<List<Recommendation>> =
        repository.refresh(favorites)
}
