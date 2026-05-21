package com.movieai.app.data.remote.gemini

import com.movieai.app.data.remote.gemini.dto.GeminiRequestDto
import com.movieai.app.data.remote.gemini.dto.GeminiResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {
    @POST("models/gemini-1.5-flash:generateContent")
    suspend fun generate(
        @Query("key") apiKey: String,
        @Body body: GeminiRequestDto,
    ): GeminiResponseDto
}
