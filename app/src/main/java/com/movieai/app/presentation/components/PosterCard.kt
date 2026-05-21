package com.movieai.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.movieai.app.domain.model.Movie
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiGradients
import com.movieai.app.presentation.theme.MovieAiTheme

/**
 * Poster tile used on Search and Favorites grids. 2:3 aspect, 14dp radius,
 * derived gradient background, optional Coil image, eyebrow WTM-### label,
 * amber heart badge when [isFavorite].
 */
@Composable
fun PosterCard(
    movie: Movie,
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wtmId = remember(movie.id) {
        "WTM-${(movie.id.mod(1000L)).toString().padStart(3, '0')}"
    }
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(14.dp))
            .background(MovieAiGradients.forMovieId(movie.id))
            .clickable(onClick = onClick),
    ) {
        movie.posterUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        // bottom gradient for text legibility
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.55f to Color.Transparent,
                        1f to MovieAiColors.bg.copy(alpha = 0.92f),
                    )
                )
            )
        )
        // top-right meta column
        Column(
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                wtmId,
                style = MaterialTheme.typography.labelSmall,
                color = MovieAiColors.text.copy(alpha = 0.5f),
            )
            if (isFavorite) {
                Spacer(Modifier.height(4.dp))
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "즐겨찾기됨",
                    tint = MovieAiColors.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        // bottom title block
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)) {
            Text(
                movie.title,
                color = MovieAiColors.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
            )
            val sub = "${movie.originalTitle.uppercase()} · ${movie.year ?: ""}".trim(' ', '·')
            if (sub.isNotBlank()) {
                Text(
                    sub,
                    color = MovieAiColors.text.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 200, heightDp = 320)
@Composable
private fun PreviewPosterCard() {
    MovieAiTheme {
        PosterCard(
            movie = Movie(
                id = 496243L,
                title = "기생충",
                originalTitle = "Parasite",
                year = 2019,
                posterUrl = null,
                backdropUrl = null,
                rating = 8.5,
                genres = listOf("스릴러", "드라마"),
            ),
            isFavorite = true,
            onClick = {},
            modifier = Modifier.padding(20.dp).width(160.dp),
        )
    }
}
