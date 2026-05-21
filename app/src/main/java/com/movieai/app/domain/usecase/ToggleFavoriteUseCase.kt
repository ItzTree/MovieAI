package com.movieai.app.domain.usecase

import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Add [movie] to favorites if it isn't yet, or remove it if it is.
 * Single entry point for the heart toggle — no separate add/remove.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movie: Movie) =
        repository.toggleFavorite(movie)
}
