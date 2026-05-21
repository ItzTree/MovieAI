package com.movieai.app.presentation.detail

import com.movieai.app.domain.model.MovieDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val detail: MovieDetail, val isFavorite: Boolean) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
