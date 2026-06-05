package com.movieai.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieai.app.domain.model.MovieDetail
import com.movieai.app.domain.usecase.GetMovieDetailUseCase
import com.movieai.app.domain.usecase.ObserveIsFavoriteUseCase
import com.movieai.app.domain.usecase.ToggleFavoriteUseCase
import com.movieai.app.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetail: GetMovieDetailUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    observeIsFavorite: ObserveIsFavoriteUseCase,
) : ViewModel() {

    private val movieId: Long =
        checkNotNull(savedStateHandle.get<Long>(Screen.Detail.ARG_ID)) { "missing movie id" }

    private val detailResult = MutableStateFlow<Result<MovieDetail>?>(null)

    val state: StateFlow<DetailUiState> = combine(
        detailResult,
        observeIsFavorite(movieId),
    ) { result, isFav ->
        when {
            result == null -> DetailUiState.Loading
            result.isSuccess -> DetailUiState.Success(result.getOrThrow(), isFav)
            else -> DetailUiState.Error(
                result.exceptionOrNull()?.message ?: "영화 정보를 불러오지 못했어요",
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailUiState.Loading)

    init { load() }

    fun load() {
        viewModelScope.launch {
            detailResult.value = null
            detailResult.value = getMovieDetail(movieId)
        }
    }

    fun onToggleFavorite() {
        val movie = (state.value as? DetailUiState.Success)?.detail?.movie ?: return
        viewModelScope.launch { toggleFavorite(movie) }
    }
}
