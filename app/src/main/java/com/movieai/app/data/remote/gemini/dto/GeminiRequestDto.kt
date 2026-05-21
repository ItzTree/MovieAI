package com.movieai.app.data.remote.gemini.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequestDto(
    val contents: List<Content>,
    @SerialName("generationConfig") val generationConfig: GenerationConfig,
) {
    @Serializable
    data class Content(val parts: List<Part>)

    @Serializable
    data class Part(val text: String)

    @Serializable
    data class GenerationConfig(
        @SerialName("responseMimeType") val responseMimeType: String = "application/json",
    )
}
