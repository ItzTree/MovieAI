package com.movieai.app.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.movieai.app.presentation.theme.MovieAiColors

/** Placeholder — full UI lands in Phase H. */
@Composable
fun FavoritesScreen(onMovieClick: (Long) -> Unit, onAiRecClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("보관함 (TODO)", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
    }
}
