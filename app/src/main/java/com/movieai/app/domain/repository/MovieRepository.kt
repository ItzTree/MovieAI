package com.movieai.app.domain.repository

import androidx.paging.PagingData
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

/**
 * Contract for movie data — TMDB-backed in the real implementation,
 * fakeable in tests. Note: [PagingData] is the one pragmatic leak of
 * `androidx.paging` into the domain layer; documented in ARCHITECTURE.md.
 */
interface MovieRepository {

    /**
     * Paged search results. When [query] is blank, the implementation
     * returns the weekly trending feed instead (the home default).
     */
    fun searchMovies(query: String): Flow<PagingData<Movie>>

    /**
     * Fetch full detail (including cast). Failures surface as
     * [Result.failure]; callers never see thrown exceptions.
     */
    suspend fun getMovieDetail(id: Long): Result<MovieDetail>

    /**
     * Insert [movie] as a favorite if absent, or remove it if already
     * favorited. Idempotent toggle semantics.
     */
    suspend fun toggleFavorite(movie: Movie)

    /** Observe the full favorites list, ordered by most-recently added. */
    fun observeFavorites(): Flow<List<Movie>>

    /** Live boolean for "is this movie currently favorited?". */
    fun isFavorite(id: Long): Flow<Boolean>
}
