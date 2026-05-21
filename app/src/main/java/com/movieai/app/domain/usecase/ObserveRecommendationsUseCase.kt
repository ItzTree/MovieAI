package com.movieai.app.domain.usecase

import com.movieai.app.domain.model.Recommendation
import com.movieai.app.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Live recommendations list from the Room cache (empty if never fetched). */
class ObserveRecommendationsUseCase @Inject constructor(
    private val repository: RecommendationRepository,
) {
    operator fun invoke(): Flow<List<Recommendation>> =
        repository.observeRecommendations()
}
