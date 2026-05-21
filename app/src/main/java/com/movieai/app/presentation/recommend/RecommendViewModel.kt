package com.movieai.app.presentation.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.usecase.ObserveFavoritesUseCase
import com.movieai.app.domain.usecase.ObserveRecommendationsUseCase
import com.movieai.app.domain.usecase.RefreshRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val observeFavorites: ObserveFavoritesUseCase,
    observeRecs: ObserveRecommendationsUseCase,
    private val refreshRecs: RefreshRecommendationsUseCase,
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    val state: StateFlow<RecommendUiState> = combine(
        observeFavorites(),
        observeRecs(),
        isRefreshing,
        error,
    ) { favs, recs, refreshing, err ->
        RecommendUiState(
            favoriteCount = favs.size,
            recommendations = recs,
            isRefreshing = refreshing,
            error = err,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RecommendUiState())

    init {
        // Auto-fire once on screen entry when conditions are met — honors the
        // user's intent when they tapped "AI 추천 받기" from the Favorites CTA.
        viewModelScope.launch {
            val favs = observeFavorites().first()
            val cache = observeRecs().first()
            if (favs.size >= RecommendUiState.MIN_FAVS && cache.isEmpty()) {
                doRefresh(favs)
            }
        }
    }

    fun onRefreshClick() {
        viewModelScope.launch {
            val favs = observeFavorites().first()
            if (favs.size >= RecommendUiState.MIN_FAVS) doRefresh(favs)
        }
    }

    private suspend fun doRefresh(favs: List<Movie>) {
        isRefreshing.value = true
        error.value = null
        val result = refreshRecs(favs)
        isRefreshing.value = false
        result.onFailure {
            error.value = it.message ?: "추천을 가져오지 못했어요"
        }
    }
}
