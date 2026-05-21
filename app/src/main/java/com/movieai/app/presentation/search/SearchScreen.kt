package com.movieai.app.presentation.search

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
fun SearchScreen(onMovieClick: (Long) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("탐색 (TODO)", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
    }
}
