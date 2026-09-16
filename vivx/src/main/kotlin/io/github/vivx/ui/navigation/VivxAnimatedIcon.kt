package io.github.vivx.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Animated icon wrapper for [VivxFloatingNavBar] tabs.
 * Supports smooth icon morphing, scale bounce, and animated tint transitions.
 *
 * Can also be paired with external Lottie compositions (e.g. `LottieAnimation(composition)`)
 * by placing the Lottie composable directly inside [VivxNavItem.icon].
 *
 * @param selectedIcon Vector icon shown when the tab is active.
 * @param unselectedIcon Vector icon shown when the tab is inactive.
 * @param isSelected Selection state passed from [VivxFloatingNavBar].
 * @param modifier Layout modifier.
 * @param size Icon dimensions (defaults to 22.dp).
 */
@Composable
fun VivxAnimatedIcon(
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    activeTint: Color = MaterialTheme.colorScheme.primary,
    inactiveTint: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "VivxAnimatedIconScale"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) activeTint else inactiveTint,
        animationSpec = tween(durationMillis = 200),
        label = "VivxAnimatedIconColor"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = null,
            tint = animatedColor,
            modifier = Modifier
                .size(size)
                .scale(animatedScale)
        )
    }
}
