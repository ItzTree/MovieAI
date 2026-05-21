package com.movieai.app.domain.model

/**
 * A single cast member shown in the Detail screen's "출연진" row.
 *
 * @property id TMDB person id.
 * @property name Actor's display name (localized when TMDB provides ko-KR).
 * @property character Character name, or `null` if uncredited.
 * @property profileUrl TMDB profile photo URL (w185), or `null` if no photo —
 *           the CastTile component falls back to a colored initial avatar.
 */
data class CastMember(
    val id: Long,
    val name: String,
    val character: String?,
    val profileUrl: String?,
)
