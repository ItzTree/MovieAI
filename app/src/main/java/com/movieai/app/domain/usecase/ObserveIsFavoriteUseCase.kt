package com.movieai.app.domain.usecase

import com.movieai.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Live favorite status for a single movie id, backed by the local store. */
class ObserveIsFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    operator fun invoke(id: Long): Flow<Boolean> = repository.isFavorite(id)
}
