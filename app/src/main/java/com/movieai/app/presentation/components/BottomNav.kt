package com.movieai.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movieai.app.presentation.theme.MovieAiColors
import com.movieai.app.presentation.theme.MovieAiTheme

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

val MovieAiBottomNavItems = listOf(
    BottomNavItem("search",     "탐색",    Icons.Default.Search),
    BottomNavItem("favorites",  "보관함",  Icons.Default.Favorite),
    BottomNavItem("recommend",  "AI 추천", Icons.Default.AutoAwesome),
)

@Composable
fun BottomNav(currentRoute: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(MovieAiColors.bg).navigationBarsPadding()) {
        HorizontalDivider(thickness = 1.dp, color = MovieAiColors.border)
        Row(
            modifier = Modifier.fillMaxWidth().height(68.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            MovieAiBottomNavItems.forEach { item ->
                val active = currentRoute.startsWith(item.route)
                val tint = if (active) MovieAiColors.primary else MovieAiColors.textDim.copy(alpha = 0.7f)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onSelect(item.route) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(item.icon, null, tint = tint, modifier = Modifier.size(26.dp))
                    Spacer(Modifier.height(5.dp))
                    Text(item.label, color = tint, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFF0D0F1A, showBackground = true)
@Composable
private fun PreviewBottomNav() {
    MovieAiTheme {
        BottomNav(currentRoute = "favorites", onSelect = {})
    }
}
