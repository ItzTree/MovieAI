package com.movieai.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors

/**
 * Small tappable heart used in card metadata rows. Always renders a filled
 * amber heart — callers decide whether to show it (e.g., only when favorited).
 * Tap area is expanded to ~30dp via padding so the 14dp icon stays compact.
 */
@Composable
fun FavoriteHeart(
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onToggle()
            }
            .padding(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "즐겨찾기 해제",
            tint = MovieAiColors.primary,
            modifier = Modifier.size(14.dp),
        )
    }
}
