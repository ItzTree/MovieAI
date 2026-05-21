package com.movieai.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Cached row from the most recent Gemini call. Refresh wipes the table
 * and re-inserts 5 rows in a single transaction, so [rank] is a stable PK.
 */
@Entity(tableName = "recommendations")
data class RecommendationEntity(
    @PrimaryKey val rank: Int,
    val movieId: Long,
    val title: String,
    val originalTitle: String,
    val year: Int?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val rating: Double,
    val genres: String,
    val reason: String,
    val fetchedAt: Long,
)
