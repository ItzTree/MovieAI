package com.movieai.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

enum class CTAVariant { Primary, Secondary, Danger }

@Composable
fun CTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    variant: CTAVariant = CTAVariant.Primary,
    enabled: Boolean = true,
) {
    val bg: Color = when (variant) {
        CTAVariant.Primary   -> MovieAiColors.primary
        CTAVariant.Secondary -> MovieAiColors.surface
        CTAVariant.Danger    -> MovieAiColors.danger
    }
    val fg: Color = when (variant) {
        CTAVariant.Primary   -> MovieAiColors.bg
        CTAVariant.Secondary -> MovieAiColors.text
        CTAVariant.Danger    -> MovieAiColors.text
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bg.copy(alpha = if (enabled) 1f else 0.5f),
        modifier = modifier.height(48.dp).clickable(enabled = enabled, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            leadingIcon?.let {
                Icon(it, null, tint = fg, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, color = fg, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewCTAButtons() {
    MovieAiTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CTAButton("▶ 예고편 재생", {}, modifier = Modifier.fillMaxWidth(), leadingIcon = Icons.Default.PlayArrow)
            CTAButton("다시 시도", {}, modifier = Modifier.fillMaxWidth(), variant = CTAVariant.Secondary)
            CTAButton("삭제", {}, modifier = Modifier.fillMaxWidth(), variant = CTAVariant.Danger)
        }
    }
}
