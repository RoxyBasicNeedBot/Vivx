package io.github.vivx.ui.animation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Adds an ultra-satisfying tactile spring scale-down effect when an element is pressed.
 *
 * @param scaleDown The scale factor applied while holding down (e.g. 0.93f = 7% shrink).
 * @param onClick Optional click callback.
 */
@Composable
fun Modifier.vivxTactileClick(
    scaleDown: Float = 0.94f,
    onClick: (() -> Unit)? = null
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 450f),
        label = "VivxTactileScale"
    )

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = false),
            onClick = onClick
        )
    } else Modifier

    return this
        .scale(animatedScale)
        .then(clickableModifier)
}

/**
 * Expandable caption/description component for media details.
 * Supports smooth animation and "Show more" / "Show less" toggling.
 */
@Composable
fun VivxExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = style
        )

        Text(
            text = if (isExpanded) "Show less" else "Show more",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
