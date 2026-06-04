package com.movieai.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieai.app.domain.model.Movie
import com.movieai.app.domain.usecase.ObserveFavoritesUseCase
import com.movieai.app.domain.usecase.SearchMoviesUseCase
import com.movieai.app.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    searchMovies: SearchMoviesUseCase,
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    val state: StateFlow<SearchUiState> = combine(
        queryFlow,
        observeFavorites().map { list -> list.map { it.id }.toSet() }.onStart { emit(emptySet()) },
    ) { q, favIds ->
        SearchUiState(query = q, favoriteIds = favIds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState(),
    )

    val pagingData: Flow<PagingData<Movie>> = queryFlow
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { q -> searchMovies(q) }
        .cachedIn(viewModelScope)

    fun onQueryChange(q: String) { queryFlow.value = q }
    fun onToggleFavorite(movie: Movie) {
        viewModelScope.launch { toggleFavorite(movie) }
    }
}
