package com.movieai.app.domain.model

/**
 * Aggregated detail view of a movie used on the Detail screen.
 *
 * Wraps the base [Movie] (so callers can reuse summary rendering) and
 * adds the heavier fields fetched only on detail view.
 *
 * @property movie The base summary; its `genres` list is populated for detail.
 * @property overview Long-form synopsis ("개요").
 * @property runtimeMinutes Runtime in minutes, `null` if unknown.
 * @property cast Top cast members, already sorted by billing order.
 */
data class MovieDetail(
    val movie: Movie,
    val overview: String,
    val runtimeMinutes: Int?,
    val cast: List<CastMember>,
)
