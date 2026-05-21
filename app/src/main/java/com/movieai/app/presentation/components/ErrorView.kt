package com.movieai.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(message, color = MovieAiColors.textDim, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        CTAButton(text = "다시 시도", onClick = onRetry, variant = CTAVariant.Secondary)
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 360, heightDp = 480)
@Composable
private fun PreviewErrorView() {
    MovieAiTheme {
        ErrorView(message = "네트워크에 연결할 수 없어요", onRetry = {})
    }
}
