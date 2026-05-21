package com.movieai.app.data.remote.gemini.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponseDto(
    val candidates: List<Candidate> = emptyList(),
) {
    @Serializable
    data class Candidate(val content: Content)

    @Serializable
    data class Content(val parts: List<Part>)

    @Serializable
    data class Part(val text: String = "")
}
