package com.movieai.app.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.movieai.app.domain.model.Movie
import com.movieai.app.presentation.components.ErrorView
import com.movieai.app.presentation.components.FavoriteHeart
import com.movieai.app.presentation.components.GenreChip
import com.movieai.app.presentation.components.PosterCardSkeleton
import com.movieai.app.presentation.components.PosterCard
import com.movieai.app.presentation.components.RatingPill
import com.movieai.app.presentation.components.SearchField
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

@Composable
fun SearchScreen(
    onMovieClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyItems = viewModel.pagingData.collectAsLazyPagingItems()

    SearchContent(
        state = state,
        lazyItems = lazyItems,
        onQueryChange = viewModel::onQueryChange,
        onGenreSelect = viewModel::onGenreSelect,
        onMovieClick = onMovieClick,
        onToggleFavorite = viewModel::onToggleFavorite,
    )
}

@Composable
private fun SearchContent(
    state: SearchUiState,
    lazyItems: androidx.paging.compose.LazyPagingItems<Movie>,
    onQueryChange: (String) -> Unit,
    onGenreSelect: (String) -> Unit,
    onMovieClick: (Long) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        Text("오늘 뭐 보지?", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(16.dp))
        SearchField(query = state.query, onQueryChange = onQueryChange)

        Spacer(Modifier.height(14.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(MovieAiGenres) { g ->
                GenreChip(label = g, selected = state.selectedGenre == g) { onGenreSelect(g) }
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("이번 주 트렌딩", color = MovieAiColors.text, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            Text("전체 보기", color = MovieAiColors.textDim, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(12.dp))

        val refresh = lazyItems.loadState.refresh
        when {
            refresh is LoadState.Error -> {
                ErrorView(
                    message = refresh.error.message ?: "결과를 불러오지 못했어요",
                    onRetry = { lazyItems.retry() },
                )
            }
            refresh is LoadState.Loading && lazyItems.itemCount == 0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                ) {
                    items(count = 6) { PosterCardSkeleton() }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                ) {
                    items(count = lazyItems.itemCount, key = { idx -> lazyItems[idx]?.id ?: idx }) { idx ->
                        val movie = lazyItems[idx] ?: return@items
                        Column {
                            PosterCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) },
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                movie.title,
                                color = MovieAiColors.text,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                            )
                            Spacer(Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    movie.year?.toString() ?: "—",
                                    color = MovieAiColors.textDim,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Spacer(Modifier.width(8.dp))
                                RatingPill(rating = movie.rating)
                                if (movie.id in state.favoriteIds) {
                                    FavoriteHeart(onToggle = { onToggleFavorite(movie) })
                                }
                            }
                        }
                    }
                    if (lazyItems.loadState.append is LoadState.Loading) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(60.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("불러오는 중…", color = MovieAiColors.textDim, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 412, heightDp = 700)
@Composable
private fun PreviewSearchChrome() {
    MovieAiTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text("오늘 뭐 보지?", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            SearchField(query = "", onQueryChange = {})
            Spacer(Modifier.height(14.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MovieAiGenres) { g -> GenreChip(g, g == "전체") {} }
            }
        }
    }
}
