package com.movieai.app.presentation.recommend

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
fun RecommendScreen(onMovieClick: (Long) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("AI 추천 (TODO)", color = MovieAiColors.text, style = MaterialTheme.typography.headlineLarge)
    }
}
