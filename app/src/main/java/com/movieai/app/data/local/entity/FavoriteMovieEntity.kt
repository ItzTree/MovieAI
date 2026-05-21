package com.movieai.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val originalTitle: String,
    val year: Int?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val rating: Double,
    /** Pipe-joined genre names — Room has no native `List<String>` support. */
    val genres: String,
    val addedAt: Long,
)
