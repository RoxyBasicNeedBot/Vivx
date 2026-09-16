package io.github.vivx.ui.shimmer

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Applies a fluid animated shimmer gradient to create modern loading skeletons.
 */
@Composable
fun Modifier.vivxShimmer(
    shape: Shape = RoundedCornerShape(8.dp),
    baseColor: Color? = null,
    highlightColor: Color? = null
): Modifier {
    val defaultBase = baseColor ?: MaterialTheme.colorScheme.surfaceContainerHigh
    val defaultHighlight = highlightColor ?: MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f)

    val transition = rememberInfiniteTransition(label = "VivxShimmerTransition")
    val translateAnim = transition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "VivxShimmerTranslate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(defaultBase, defaultHighlight, defaultBase),
        start = Offset(translateAnim.value - 300f, translateAnim.value - 300f),
        end = Offset(translateAnim.value + 300f, translateAnim.value + 300f)
    )

    return this
        .clip(shape)
        .background(shimmerBrush)
}

/**
 * Placeholder box that animates with a fluid shimmer.
 */
@Composable
fun VivxShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(modifier = modifier.vivxShimmer(shape = shape))
}
