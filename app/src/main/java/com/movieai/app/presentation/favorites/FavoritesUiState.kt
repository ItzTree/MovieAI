package com.movieai.app.presentation.favorites

import com.movieai.app.domain.model.Movie

sealed interface FavoritesUiState {
    data object Empty : FavoritesUiState
    data class Success(val items: List<Movie>) : FavoritesUiState
}
