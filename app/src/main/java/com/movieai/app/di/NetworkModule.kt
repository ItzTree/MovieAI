package com.movieai.app.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.movieai.app.BuildConfig
import com.movieai.app.data.remote.gemini.GeminiApi
import com.movieai.app.data.remote.tmdb.TmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class TmdbRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class GeminiRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
            else HttpLoggingInterceptor.Level.NONE
        }

    @Provides @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val tmdbAuth = Interceptor { chain ->
            val req = chain.request()
            val authed = if (req.url.host == "api.themoviedb.org") {
                req.newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                    .addHeader("accept", "application/json")
                    .build()
            } else req
            chain.proceed(authed)
        }
        return OkHttpClient.Builder()
            .addInterceptor(tmdbAuth)
            .addInterceptor(logging)
            .build()
    }

    @Provides @Singleton @TmdbRetrofit
    fun provideTmdbRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides @Singleton @GeminiRetrofit
    fun provideGeminiRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides @Singleton
    fun provideTmdbApi(@TmdbRetrofit retrofit: Retrofit): TmdbApi =
        retrofit.create(TmdbApi::class.java)

    @Provides @Singleton
    fun provideGeminiApi(@GeminiRetrofit retrofit: Retrofit): GeminiApi =
        retrofit.create(GeminiApi::class.java)
}
