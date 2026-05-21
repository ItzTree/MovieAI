package com.movieai.app.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movieai.app.data.mapper.toDomain
import com.movieai.app.data.remote.tmdb.TmdbApi
import com.movieai.app.domain.model.Movie

/**
 * PagingSource backing the Search screen's grid. Switches between TMDB
 * `/search/movie` and `/trending/movie/week` based on [mode] — a blank
 * query falls back to trending and stops after the first page.
 */
class MovieSearchPagingSource(
    private val api: TmdbApi,
    private val query: String,
    private val mode: Mode,
) : PagingSource<Int, Movie>() {

    enum class Mode { Search, Trending }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> = runCatching {
        val page = params.key ?: 1
        val resp = when (mode) {
            Mode.Search   -> api.searchMovies(query = query, page = page)
            Mode.Trending -> api.trending()
        }
        val data = resp.results.map { it.toDomain() }
        val nextKey = when {
            mode == Mode.Trending -> null
            page >= resp.totalPages -> null
            else -> page + 1
        }
        LoadResult.Page(
            data    = data,
            prevKey = if (page == 1) null else page - 1,
            nextKey = nextKey,
        )
    }.getOrElse { LoadResult.Error(it) }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}
