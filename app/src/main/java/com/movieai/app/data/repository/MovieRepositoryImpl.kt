package com.movieai.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movieai.app.data.local.FavoriteDao
import com.movieai.app.data.mapper.toDomain
import com.movieai.app.data.mapper.toFavoriteEntity
import com.movieai.app.data.remote.paging.MovieSearchPagingSource
import com.movieai.app.data.remote.tmdb.TmdbApi
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.MovieDetail
import com.movieai.app.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val favoriteDao: FavoriteDao,
) : MovieRepository {

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        val mode = if (query.isBlank()) {
            MovieSearchPagingSource.Mode.Trending
        } else {
            MovieSearchPagingSource.Mode.Search
        }
        return Pager(PagingConfig(pageSize = 20, prefetchDistance = 4)) {
            MovieSearchPagingSource(api, query.trim(), mode)
        }.flow
    }

    override suspend fun getMovieDetail(id: Long): Result<MovieDetail> =
        withContext(Dispatchers.IO) {
            runCatching {
                val detail = api.movieDetail(id)
                val credits = api.movieCredits(id)
                detail.toDomain(credits.cast)
            }
        }

    override suspend fun toggleFavorite(movie: Movie) {
        withContext(Dispatchers.IO) {
            if (favoriteDao.existsNow(movie.id)) {
                favoriteDao.deleteById(movie.id)
            } else {
                favoriteDao.insert(movie.toFavoriteEntity())
            }
        }
    }

    override fun observeFavorites(): Flow<List<Movie>> =
        favoriteDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun isFavorite(id: Long): Flow<Boolean> = favoriteDao.isFavorite(id)
}
