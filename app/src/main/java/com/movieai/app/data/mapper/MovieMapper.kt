package com.movieai.app.data.mapper

import com.movieai.app.data.remote.tmdb.dto.CastDto
import com.movieai.app.data.remote.tmdb.dto.MovieDetailDto
import com.movieai.app.data.remote.tmdb.dto.MovieDto
import com.movieai.app.domain.model.CastMember
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.MovieDetail

private const val POSTER_BASE   = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE = "https://image.tmdb.org/t/p/w780"
private const val PROFILE_BASE  = "https://image.tmdb.org/t/p/w185"

private fun String?.toYear(): Int? =
    this?.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull()

fun MovieDto.toDomain(): Movie = Movie(
    id            = id,
    title         = title.ifBlank { originalTitle },
    originalTitle = originalTitle,
    year          = releaseDate.toYear(),
    posterUrl     = posterPath?.let { POSTER_BASE + it },
    backdropUrl   = backdropPath?.let { BACKDROP_BASE + it },
    rating        = voteAverage,
)

fun MovieDetailDto.toDomain(cast: List<CastDto>): MovieDetail {
    val base = Movie(
        id            = id,
        title         = title.ifBlank { originalTitle },
        originalTitle = originalTitle,
        year          = releaseDate.toYear(),
        posterUrl     = posterPath?.let { POSTER_BASE + it },
        backdropUrl   = backdropPath?.let { BACKDROP_BASE + it },
        rating        = voteAverage,
        genres        = genres.map { it.name },
    )
    return MovieDetail(
        movie          = base,
        overview       = overview,
        runtimeMinutes = runtime,
        cast = cast.sortedBy { it.order }.take(12).map {
            CastMember(
                id         = it.id,
                name       = it.name,
                character  = it.character,
                profileUrl = it.profilePath?.let { p -> PROFILE_BASE + p },
            )
        },
    )
}
