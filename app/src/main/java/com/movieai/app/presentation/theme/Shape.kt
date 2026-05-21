package com.movieai.app.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val MovieAiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(14.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(999.dp),
)

/**
 * Allowed spacing units from the design handoff (multiples of 4dp).
 * Screens should reference these constants instead of magic numbers.
 */
object MovieAiSpacing {
    val xs      = 10.dp
    val s       = 12.dp
    val m       = 14.dp
    val l       = 16.dp
    val xl      = 20.dp
    val xxl     = 24.dp
    val gridGap = 12.dp
    val safeTop = 56.dp
}
