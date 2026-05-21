package com.movieai.app.presentation.recommend

import com.movieai.app.domain.model.Recommendation

data class RecommendUiState(
    val favoriteCount: Int = 0,
    val recommendations: List<Recommendation> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null,
) {
    val isUnlocked: Boolean get() = favoriteCount >= MIN_FAVS
    val hasCache: Boolean get() = recommendations.isNotEmpty()

    companion object {
        const val MIN_FAVS = 3
    }
}
