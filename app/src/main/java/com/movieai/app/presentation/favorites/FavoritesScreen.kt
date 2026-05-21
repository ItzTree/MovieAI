package com.movieai.app.presentation.favorites

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movieai.app.domain.model.Movie
import com.movieai.app.presentation.components.EmptyState
import com.movieai.app.presentation.components.PosterCard
import com.movieai.app.presentation.components.RatingPill
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

@Composable
fun FavoritesScreen(
    onMovieClick: (Long) -> Unit,
    onAiRecClick: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoritesContent(
        state = state,
        onMovieClick = onMovieClick,
        onAiRecClick = onAiRecClick,
        onRemove = viewModel::remove,
    )
}

@Composable
private fun FavoritesContent(
    state: FavoritesUiState,
    onMovieClick: (Long) -> Unit,
    onAiRecClick: () -> Unit,
    onRemove: (Movie) -> Unit,
) {
    var pendingRemoval by remember { mutableStateOf<Movie?>(null) }
    val items = (state as? FavoritesUiState.Success)?.items.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("보관함", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.weight(1f))
            Text(
                "${items.size}편",
                color = MovieAiColors.textDim,
                style = MaterialTheme.typography.labelMedium,
            )
        }

        when (state) {
            FavoritesUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyState(
                        icon = Icons.Default.FavoriteBorder,
                        title = "보관함이 비어있어요",
                        subtitle = "탐색에서 마음에 드는 영화를 추가해보세요",
                    )
                }
            }
            is FavoritesUiState.Success -> {
                Spacer(Modifier.height(16.dp))
                AiRecCTA(favoriteCount = items.size, onClick = onAiRecClick)
                Spacer(Modifier.height(20.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                ) {
                    items(items = items, key = { it.id }) { movie ->
                        Column {
                            PosterCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) },
                                onLongClick = { pendingRemoval = movie },
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
                            }
                        }
                    }
                }
            }
        }
    }

    pendingRemoval?.let { movie ->
        AlertDialog(
            onDismissRequest = { pendingRemoval = null },
            containerColor = MovieAiColors.surface,
            titleContentColor = MovieAiColors.text,
            textContentColor = MovieAiColors.textDim,
            title = { Text("삭제할까요?") },
            text = { Text("‘${movie.title}’ 을(를) 보관함에서 뺍니다.") },
            confirmButton = {
                TextButton(onClick = {
                    onRemove(movie)
                    pendingRemoval = null
                }) {
                    Text("삭제", color = MovieAiColors.danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingRemoval = null }) {
                    Text("취소", color = MovieAiColors.text)
                }
            },
        )
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 412, heightDp = 800)
@Composable
private fun PreviewFavoritesEmpty() {
    MovieAiTheme {
        FavoritesContent(
            state = FavoritesUiState.Empty,
            onMovieClick = {}, onAiRecClick = {}, onRemove = {},
        )
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 412, heightDp = 800)
@Composable
private fun PreviewFavoritesSuccess() {
    val sample = listOf(
        Movie(496243L, "기생충", "Parasite", 2019, null, null, 8.5, listOf("스릴러")),
        Movie(155L, "다크 나이트", "The Dark Knight", 2008, null, null, 9.0, listOf("액션")),
    )
    MovieAiTheme {
        FavoritesContent(
            state = FavoritesUiState.Success(sample),
            onMovieClick = {}, onAiRecClick = {}, onRemove = {},
        )
    }
}
