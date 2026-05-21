package com.movieai.app.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.movieai.app.domain.model.Movie
import com.movieai.app.presentation.theme.MovieAiGradients
import com.movieai.app.presentation.theme.MovieAiTheme

/**
 * Poster tile — 2:3 image only. Per-movie gradient fills the card before
 * the network image loads (or stays visible if the movie has no poster).
 * All metadata (title, year, rating, favorite indicator) is rendered by
 * the parent layout so the card stays a clean image surface.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    val gestureModifier = if (onLongClick != null) {
        Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)
    } else {
        Modifier.clickable(onClick = onClick)
    }
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(14.dp))
            .background(MovieAiGradients.forMovieId(movie.id))
            .then(gestureModifier),
    ) {
        movie.posterUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
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
            onClick = {},
            modifier = Modifier.padding(20.dp).width(160.dp),
        )
    }
}
