package io.github.vivx.ui.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a premium frosted liquid-glass aesthetic with subtle specular border highlights.
 *
 * @param shape Shape of the glass surface (defaults to RoundedCornerShape(16.dp)).
 * @param backgroundAlpha Opacity of the frosted surface background.
 * @param borderAlpha Opacity of the glass highlight border.
 */
@Composable
fun Modifier.vivxGlass(
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundAlpha: Float = 0.72f,
    borderAlpha: Float = 0.35f,
    tintColor: Color? = null
): Modifier {
    val surfaceColor = tintColor ?: MaterialTheme.colorScheme.surfaceContainerHigh
    val borderGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = borderAlpha),
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = borderAlpha * 0.4f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = borderAlpha * 0.1f)
        )
    )

    return this
        .clip(shape)
        .background(surfaceColor.copy(alpha = backgroundAlpha))
        .border(width = 1.dp, brush = borderGradient, shape = shape)
}

/**
 * Frosted liquid glass card container for modern media apps.
 */
@Composable
fun VivxGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundAlpha: Float = 0.75f,
    borderAlpha: Float = 0.35f,
    tintColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(),
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .vivxGlass(
                shape = shape,
                backgroundAlpha = backgroundAlpha,
                borderAlpha = borderAlpha,
                tintColor = tintColor
            )
            .then(clickableModifier),
        content = content
    )
}

/**
 * Frosted floating pill container (ideal for floating navigation, quick actions, filter tags).
 */
@Composable
fun VivxGlassPill(
    modifier: Modifier = Modifier,
    tintColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    VivxGlassCard(
        modifier = modifier,
        shape = CircleShape,
        backgroundAlpha = 0.88f,
        borderAlpha = 0.40f,
        tintColor = tintColor,
        onClick = onClick,
        content = content
    )
}
