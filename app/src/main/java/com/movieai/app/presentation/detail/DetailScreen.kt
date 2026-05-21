package com.movieai.app.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.movieai.app.domain.model.MovieDetail
import com.movieai.app.presentation.components.CastTile
import com.movieai.app.presentation.components.ErrorView
import com.movieai.app.presentation.components.FavoriteToggle
import com.movieai.app.presentation.components.GenreChip
import com.movieai.app.presentation.components.RatingPill
import com.movieai.app.presentation.theme.MovieAiColors

@Composable
fun DetailScreen(
    id: Long,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetailContent(
        state = state,
        onBack = onBack,
        onToggleFavorite = viewModel::onToggleFavorite,
        onRetry = viewModel::load,
    )
}

@Composable
private fun DetailContent(
    state: DetailUiState,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            DetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MovieAiColors.primary)
                }
            }
            is DetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorView(message = state.message, onRetry = onRetry)
                }
            }
            is DetailUiState.Success -> {
                DetailBody(detail = state.detail)
            }
        }

        // Top overlay — back button always, favorite toggle when loaded
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackButton(onClick = onBack)
            if (state is DetailUiState.Success) {
                FavoriteToggle(
                    isFavorite = state.isFavorite,
                    onToggle = onToggleFavorite,
                )
            }
        }
    }
}

@Composable
private fun DetailBody(detail: MovieDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Backdrop with bottom fade into bg
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            detail.movie.backdropUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = detail.movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MovieAiColors.bg.copy(alpha = 0.2f),
                                MovieAiColors.bg.copy(alpha = 0.0f),
                                MovieAiColors.bg,
                            ),
                        ),
                    ),
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                detail.movie.title,
                color = MovieAiColors.text,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            )
            detail.movie.originalTitle?.takeIf { it != detail.movie.title }?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, color = MovieAiColors.textDim, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    detail.movie.year?.toString() ?: "—",
                    color = MovieAiColors.textDim,
                    style = MaterialTheme.typography.labelMedium,
                )
                detail.runtimeMinutes?.let { mins ->
                    Spacer(Modifier.width(8.dp))
                    Text("·", color = MovieAiColors.textDim, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        formatRuntime(mins),
                        color = MovieAiColors.textDim,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(Modifier.width(12.dp))
                RatingPill(rating = detail.movie.rating)
            }

            if (detail.movie.genres.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(detail.movie.genres) { g ->
                        GenreChip(label = g, selected = false, onClick = {})
                    }
                }
            }

            if (detail.overview.isNotBlank()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "줄거리",
                    color = MovieAiColors.text,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    detail.overview,
                    color = MovieAiColors.text.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            if (detail.cast.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "출연진",
                    color = MovieAiColors.text,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(detail.cast.take(12)) { member -> CastTile(member = member) }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MovieAiColors.surface.copy(alpha = 0.72f),
        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로",
                tint = MovieAiColors.text,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

private fun formatRuntime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return when {
        h == 0 -> "${m}분"
        m == 0 -> "${h}시간"
        else -> "${h}시간 ${m}분"
    }
}
