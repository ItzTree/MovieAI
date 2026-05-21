package com.movieai.app.data.remote.tmdb

import com.movieai.app.data.remote.tmdb.dto.CreditsDto
import com.movieai.app.data.remote.tmdb.dto.MovieDetailDto
import com.movieai.app.data.remote.tmdb.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for TMDB v3 endpoints. Authentication (v4 Bearer
 * token) is injected by an OkHttp interceptor configured in NetworkModule,
 * so individual method signatures stay free of auth params.
 */
interface TmdbApi {

    @GET("trending/movie/week")
    suspend fun trending(
        @Query("language") language: String = "ko-KR",
    ): SearchResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("include_adult") includeAdult: Boolean = false,
    ): SearchResponseDto

    @GET("movie/{id}")
    suspend fun movieDetail(
        @Path("id") id: Long,
        @Query("language") language: String = "ko-KR",
    ): MovieDetailDto

    @GET("movie/{id}/credits")
    suspend fun movieCredits(
        @Path("id") id: Long,
        @Query("language") language: String = "ko-KR",
    ): CreditsDto
}
