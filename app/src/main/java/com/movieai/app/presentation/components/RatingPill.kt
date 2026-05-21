package com.movieai.app.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

@Composable
fun RatingPill(rating: Double, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MovieAiColors.surface,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Icon(Icons.Default.Star, null, tint = MovieAiColors.primary, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                "%.1f".format(rating),
                color = MovieAiColors.text,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewRatingPill() {
    MovieAiTheme {
        RatingPill(8.5, modifier = Modifier.padding(20.dp))
    }
}
