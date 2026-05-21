package com.movieai.app.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.usecase.ObserveFavoritesUseCase
import com.movieai.app.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    val state: StateFlow<FavoritesUiState> = observeFavorites()
        .map<List<Movie>, FavoritesUiState> { list ->
            if (list.isEmpty()) FavoritesUiState.Empty
            else FavoritesUiState.Success(list)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesUiState.Empty)

    fun remove(movie: Movie) {
        viewModelScope.launch { toggleFavorite(movie) }
    }
}
