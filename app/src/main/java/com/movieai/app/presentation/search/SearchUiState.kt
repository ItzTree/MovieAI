package com.movieai.app.presentation.search

/**
 * State for the Search screen chrome (query field, selected genre,
 * favorite badges). Paging data flows separately on the ViewModel
 * because it's a `Flow<PagingData>`, not a discrete UI state.
 */
data class SearchUiState(
    val query: String = "",
    val selectedGenre: String = "전체",
    val favoriteIds: Set<Long> = emptySet(),
)

val MovieAiGenres: List<String> =
    listOf("전체", "드라마", "SF", "스릴러", "코미디", "로맨스", "애니")
