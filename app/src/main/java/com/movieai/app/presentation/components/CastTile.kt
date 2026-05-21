package com.movieai.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.movieai.app.domain.model.CastMember
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

private val CastPalette = listOf(
    Color(0xFF7C2D12), Color(0xFF1E3A8A), Color(0xFF14532D), Color(0xFF701A75),
    Color(0xFFB45309), Color(0xFF155E75), Color(0xFF5B21B6), Color(0xFF334155),
)

@Composable
fun CastTile(member: CastMember, modifier: Modifier = Modifier) {
    val idx = ((member.name.hashCode() % CastPalette.size) + CastPalette.size) % CastPalette.size
    val bg = CastPalette[idx]
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(CircleShape).background(bg),
            contentAlignment = Alignment.Center,
        ) {
            if (member.profileUrl != null) {
                AsyncImage(
                    model = member.profileUrl,
                    contentDescription = member.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Text(
                    text = member.name.firstOrNull()?.toString() ?: "?",
                    color = MovieAiColors.text,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            member.name,
            color = MovieAiColors.text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        member.character?.let {
            Text(
                it,
                color = MovieAiColors.textDim,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewCastTile() {
    MovieAiTheme {
        CastTile(
            member = CastMember(id = 1L, name = "송강호", character = "기택", profileUrl = null),
            modifier = Modifier.padding(20.dp),
        )
    }
}
