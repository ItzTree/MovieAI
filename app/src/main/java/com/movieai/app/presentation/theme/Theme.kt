package com.movieai.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Top-level MovieAI theme wrapper. Window-level chrome (status bar
 * color, light/dark status icons) is configured via the Android XML
 * theme (`Theme.MovieAI` in res/values/themes.xml), so this composable
 * stays pure Compose.
 */
@Composable
fun MovieAiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MovieAiDarkScheme,
        typography  = MovieAiTypography,
        shapes      = MovieAiShapes,
        content     = content,
    )
}
