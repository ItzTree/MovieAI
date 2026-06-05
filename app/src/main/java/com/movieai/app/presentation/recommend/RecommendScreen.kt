package com.movieai.app.presentation.recommend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.movieai.app.domain.model.Recommendation
import com.movieai.app.presentation.components.EmptyState
import com.movieai.app.presentation.components.ErrorView
import com.movieai.app.presentation.components.RatingPill
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiGradients

@Composable
fun RecommendScreen(
    onMovieClick: (Long) -> Unit,
    viewModel: RecommendViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RecommendContent(
        state = state,
        onMovieClick = onMovieClick,
        onRefreshClick = viewModel::onRefreshClick,
    )
}

@Composable
private fun RecommendContent(
    state: RecommendUiState,
    onMovieClick: (Long) -> Unit,
    onRefreshClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("AI 추천", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(2.dp))
                Text(
                    "당신의 취향에 맞는 영화를 추천해드려요",
                    color = MovieAiColors.textDim,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            // Hide chip while the body itself is showing a loading state
            // (first-time fetch, no cache yet) to avoid double indicators.
            val bodyIsLoading = state.isRefreshing && !state.hasCache
            if (state.isUnlocked && !bodyIsLoading) {
                RefreshChip(isRefreshing = state.isRefreshing, onClick = onRefreshClick)
            }
        }
        Spacer(Modifier.height(20.dp))

        when {
            !state.isUnlocked && !state.hasCache -> LockedState(state.favoriteCount)
            state.isRefreshing && !state.hasCache -> LoadingState()
            state.error != null && !state.hasCache -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                ErrorView(message = state.error, onRetry = onRefreshClick)
            }
            state.hasCache -> Column {
                // A refresh can fail while a cached list is on screen — show the
                // error non-destructively above the old results instead of
                // swallowing it. User retries via the "다시" chip.
                if (state.error != null) {
                    RefreshErrorBanner(state.error)
                    Spacer(Modifier.height(12.dp))
                }
                RecList(state.recommendations, onMovieClick)
            }
            else -> LoadingState() // unlocked but waiting for first emission
        }
    }
}

@Composable
private fun LockedState(currentCount: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyState(
            icon = Icons.Default.AutoAwesome,
            title = "추천이 잠겨있어요",
            subtitle = "보관함에 영화를 ${RecommendUiState.MIN_FAVS}편 모으면 풀려요 (${currentCount}/${RecommendUiState.MIN_FAVS})",
        )
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = MovieAiColors.primary)
        Spacer(Modifier.height(16.dp))
        Text(
            "Gemini가 분석 중이에요…",
            color = MovieAiColors.textDim,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun RecList(items: List<Recommendation>, onMovieClick: (Long) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        items(items, key = { it.rank }) { rec ->
            RecCard(rec = rec, onClick = { onMovieClick(rec.movie.id) })
        }
    }
}

@Composable
private fun RecCard(rec: Recommendation, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MovieAiColors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MovieAiGradients.forMovieId(rec.movie.id)),
            ) {
                rec.movie.posterUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = rec.movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Text(
                    text = "#%02d".format(rec.rank),
                    color = MovieAiColors.text,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(6.dp)
                        .background(
                            MovieAiColors.bg.copy(alpha = 0.7f),
                            RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    rec.movie.title,
                    color = MovieAiColors.text,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        rec.movie.year?.toString() ?: "—",
                        color = MovieAiColors.textDim,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    if (rec.movie.rating > 0.0) {
                        Spacer(Modifier.width(8.dp))
                        RatingPill(rating = rec.movie.rating)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    rec.reason,
                    color = MovieAiColors.text.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RefreshErrorBanner(message: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MovieAiColors.danger.copy(alpha = 0.15f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            message,
            color = MovieAiColors.danger,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun RefreshChip(isRefreshing: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MovieAiColors.surface,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = !isRefreshing, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isRefreshing) {
                CircularProgressIndicator(
                    color = MovieAiColors.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(14.dp),
                )
            } else {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MovieAiColors.primary,
                    modifier = Modifier.size(16.dp),
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                if (isRefreshing) "분석 중" else "다시",
                color = MovieAiColors.text,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
