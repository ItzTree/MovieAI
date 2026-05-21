package com.movieai.app.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Midnight Noir palette for MovieAI.
 *
 * Hex values are fixed by the design handoff and must not drift.
 */
object MovieAiColors {
    val bg          = Color(0xFF0D0F1A)
    val surface     = Color(0xFF161827)
    val surface2    = Color(0xFF1F2236)
    val border      = Color(0x10FFFFFF)
    val primary     = Color(0xFFF4B942)
    val primarySoft = Color(0x26F4B942)
    val text        = Color(0xFFF5F5F0)
    val textDim     = Color(0xFF8B8DA0)
    val danger      = Color(0xFFE85A5A)
    val success     = Color(0xFF4ADE80)
}

val MovieAiDarkScheme: ColorScheme = darkColorScheme(
    background       = MovieAiColors.bg,
    onBackground     = MovieAiColors.text,
    surface          = MovieAiColors.surface,
    onSurface        = MovieAiColors.text,
    surfaceVariant   = MovieAiColors.surface2,
    onSurfaceVariant = MovieAiColors.textDim,
    primary          = MovieAiColors.primary,
    onPrimary        = MovieAiColors.bg,
    secondary        = MovieAiColors.primary,
    onSecondary      = MovieAiColors.bg,
    error            = MovieAiColors.danger,
    onError          = MovieAiColors.text,
    outline          = MovieAiColors.border,
)

/**
 * Per-movie gradient pool. The poster card background is derived from
 * [movieId] so distinct movies feel visually distinct even before the
 * Coil image loads.
 */
object MovieAiGradients {
    private val pool: List<Pair<Color, Color>> = listOf(
        Color(0xFF7C2D12) to Color(0xFF1F2236), // warm crimson
        Color(0xFFB45309) to Color(0xFF1F2236), // ochre
        Color(0xFF1E3A8A) to Color(0xFF0D0F1A), // cool blue
        Color(0xFF155E75) to Color(0xFF0D0F1A), // teal
        Color(0xFF701A75) to Color(0xFF1F2236), // magenta
        Color(0xFF14532D) to Color(0xFF0D0F1A), // forest
        Color(0xFF5B21B6) to Color(0xFF0D0F1A), // plum
        Color(0xFF334155) to Color(0xFF0D0F1A), // slate fallback
    )

    fun forMovieId(movieId: Long): Brush {
        val idx = (movieId.mod(pool.size.toLong())).toInt()
        val (top, bottom) = pool[idx]
        return Brush.verticalGradient(listOf(top, bottom))
    }
}
