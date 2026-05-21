package com.movieai.app.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

private const val MIN_FAVS_FOR_REC = 3

@Composable
fun AiRecCTA(
    favoriteCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = favoriteCount >= MIN_FAVS_FOR_REC
    val gradient = Brush.linearGradient(
        colors = listOf(
            MovieAiColors.primary.copy(alpha = if (enabled) 0.20f else 0.10f),
            MovieAiColors.primary.copy(alpha = if (enabled) 0.06f else 0.03f),
        ),
    )
    val borderAlpha = if (enabled) 0.35f else 0.15f

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .border(1.dp, MovieAiColors.primary.copy(alpha = borderAlpha), RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MovieAiColors.primary.copy(alpha = if (enabled) 1f else 0.5f),
                modifier = Modifier.size(26.dp),
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "AI 추천 받기",
                    color = MovieAiColors.text.copy(alpha = if (enabled) 1f else 0.7f),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(Modifier.height(2.dp))
                val sub = if (enabled) {
                    "취향 기반으로 5편 골라드려요"
                } else {
                    "$favoriteCount/${MIN_FAVS_FOR_REC}편 — 3편 모이면 추천이 열려요"
                }
                Text(sub, color = MovieAiColors.textDim, style = MaterialTheme.typography.bodyMedium)
            }
            if (enabled) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MovieAiColors.primary,
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true, widthDp = 372)
@Composable
private fun PreviewAiRecCTAStates() {
    MovieAiTheme {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AiRecCTA(favoriteCount = 1, onClick = {})
            AiRecCTA(favoriteCount = 3, onClick = {})
        }
    }
}
