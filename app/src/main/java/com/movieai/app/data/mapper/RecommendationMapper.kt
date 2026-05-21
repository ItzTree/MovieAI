package com.movieai.app.data.mapper

import com.movieai.app.data.local.entity.RecommendationEntity
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.model.Recommendation

private const val SEP = "|"

fun RecommendationEntity.toDomain(): Recommendation = Recommendation(
    rank   = rank,
    movie  = Movie(
        id            = movieId,
        title         = title,
        originalTitle = originalTitle,
        year          = year,
        posterUrl     = posterUrl,
        backdropUrl   = backdropUrl,
        rating        = rating,
        genres        = genres.split(SEP).filter { it.isNotBlank() },
    ),
    reason = reason,
)

fun Recommendation.toEntity(fetchedAt: Long = System.currentTimeMillis()): RecommendationEntity =
    RecommendationEntity(
        rank          = rank,
        movieId       = movie.id,
        title         = movie.title,
        originalTitle = movie.originalTitle,
        year          = movie.year,
        posterUrl     = movie.posterUrl,
        backdropUrl   = movie.backdropUrl,
        rating        = movie.rating,
        genres        = movie.genres.joinToString(SEP),
        reason        = reason,
        fetchedAt     = fetchedAt,
    )
