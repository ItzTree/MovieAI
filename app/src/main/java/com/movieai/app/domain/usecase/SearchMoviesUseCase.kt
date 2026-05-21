package com.movieai.app.domain.usecase

import androidx.paging.PagingData
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Search for movies by [query]. A blank query falls through to the
 * weekly trending feed — that's the Search screen's default state.
 */
class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<Movie>> =
        repository.searchMovies(query)
}
