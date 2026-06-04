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
        @SerialName("thinkingConfig") val thinkingConfig: ThinkingConfig = ThinkingConfig(),
        // Ceiling on output tokens — guards against runaway-long responses
        // (tail latency). Generous so the 5-item JSON never truncates, which
        // would otherwise break parsing.
        @SerialName("maxOutputTokens") val maxOutputTokens: Int = 1024,
    )

    /**
     * Disables 2.5-flash's default chain-of-thought to cut response latency
     * from ~10-30s down to ~3-5s. Budget 0 = no thinking tokens. Acceptable
     * trade-off for our task (movie taste analysis from 3+ favorites).
     */
    @Serializable
    data class ThinkingConfig(
        @SerialName("thinkingBudget") val thinkingBudget: Int = 0,
    )
}
