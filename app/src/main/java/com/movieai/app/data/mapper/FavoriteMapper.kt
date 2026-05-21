package com.movieai.app.data.mapper

import com.movieai.app.data.local.entity.FavoriteMovieEntity
import com.movieai.app.domain.model.Movie

private const val SEP = "|"

fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id            = id,
    title         = title,
    originalTitle = originalTitle,
    year          = year,
    posterUrl     = posterUrl,
    backdropUrl   = backdropUrl,
    rating        = rating,
    genres        = genres.split(SEP).filter { it.isNotBlank() },
)

fun Movie.toFavoriteEntity(addedAt: Long = System.currentTimeMillis()): FavoriteMovieEntity =
    FavoriteMovieEntity(
        id            = id,
        title         = title,
        originalTitle = originalTitle,
        year          = year,
        posterUrl     = posterUrl,
        backdropUrl   = backdropUrl,
        rating        = rating,
        genres        = genres.joinToString(SEP),
        addedAt       = addedAt,
    )
