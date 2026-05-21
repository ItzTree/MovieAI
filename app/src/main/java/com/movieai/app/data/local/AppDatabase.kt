package com.movieai.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movieai.app.data.local.entity.FavoriteMovieEntity
import com.movieai.app.data.local.entity.RecommendationEntity

@Database(
    entities = [FavoriteMovieEntity::class, RecommendationEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recommendationDao(): RecommendationDao
}
