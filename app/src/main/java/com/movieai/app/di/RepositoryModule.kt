package com.movieai.app.di

import com.movieai.app.data.repository.MovieRepositoryImpl
import com.movieai.app.data.repository.RecommendationRepositoryImpl
import com.movieai.app.domain.repository.MovieRepository
import com.movieai.app.domain.repository.RecommendationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    @Binds @Singleton
    abstract fun bindRecommendationRepository(impl: RecommendationRepositoryImpl): RecommendationRepository
}
