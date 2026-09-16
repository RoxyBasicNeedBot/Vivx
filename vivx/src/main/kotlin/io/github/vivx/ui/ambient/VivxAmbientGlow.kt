package io.github.vivx.ui.ambient

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ambient glow header that creates an immersive, atmospheric lighting effect.
 * Smoothly breathes and animates when the artwork or theme tint changes.
 *
 * @param tint Dominant color extracted from media artwork or active theme.
 * @param height Height of the ambient gradient glow (defaults to 360.dp).
 * @param alpha Max opacity of the glow at the top.
 */
@Composable
fun VivxAmbientGlow(
    modifier: Modifier = Modifier,
    tint: Color? = null,
    height: Dp = 340.dp,
    alpha: Float = 0.45f
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    val targetTone = tint ?: primaryColor

    val animatedGlowColor by animateColorAsState(
        targetValue = targetTone.copy(alpha = alpha),
        animationSpec = tween(600),
        label = "VivxAmbientGlowAnimation"
    )

    val ambientBrush = Brush.verticalGradient(
        colors = listOf(
            animatedGlowColor,
            animatedGlowColor.copy(alpha = alpha * 0.4f),
            Color.Transparent
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(ambientBrush)
    )
}
