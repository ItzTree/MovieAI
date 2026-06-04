package com.movieai.app.presentation.search

/**
 * State for the Search screen chrome (query field, favorite badges).
 * Paging data flows separately on the ViewModel because it's a
 * `Flow<PagingData>`, not a discrete UI state.
 */
data class SearchUiState(
    val query: String = "",
    val favoriteIds: Set<Long> = emptySet(),
)
