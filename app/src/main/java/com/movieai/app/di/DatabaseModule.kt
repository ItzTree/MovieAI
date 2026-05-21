package com.movieai.app.di

import android.content.Context
import androidx.room.Room
import com.movieai.app.data.local.AppDatabase
import com.movieai.app.data.local.FavoriteDao
import com.movieai.app.data.local.RecommendationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "movieai.db").build()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideRecommendationDao(db: AppDatabase): RecommendationDao = db.recommendationDao()
}
