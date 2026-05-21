package com.movieai.app.domain.usecase

import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Live favorites list — drives both Favorites screen and the heart badges. */
class ObserveFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeFavorites()
}
