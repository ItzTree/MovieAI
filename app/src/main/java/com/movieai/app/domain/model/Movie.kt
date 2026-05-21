package com.movieai.app.domain.model

/**
 * A movie summary used across search results, trending feeds, favorites, and
 * recommendations. Pure domain model — no DTO/Entity fields leak in.
 *
 * @property id TMDB movie id (also the primary key when cached locally).
 * @property title Localized (ko-KR) title; falls back to [originalTitle] if missing.
 * @property originalTitle Original-language title (typically Latin alphabet — used
 *           as the dim eyebrow line under the poster).
 * @property year Release year. `null` if the source had no release date.
 * @property posterUrl Fully-qualified TMDB w500 poster URL. `null` if no poster.
 * @property backdropUrl Fully-qualified TMDB w780 backdrop URL.
 * @property rating Vote average on a 0..10 scale.
 * @property genres Localized genre names. May be empty for list responses
 *           where only ids are returned by TMDB.
 */
data class Movie(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val year: Int?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val rating: Double,
    val genres: List<String> = emptyList(),
)
