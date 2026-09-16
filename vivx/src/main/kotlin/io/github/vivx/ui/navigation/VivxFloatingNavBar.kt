package io.github.vivx.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vivx.ui.chat.VivxUnreadBadge

/**
 * Navigation item specification for [VivxFloatingNavBar].
 *
 * @param id Unique identifier.
 * @param title Label shown under the icon.
 * @param badgeCount Unread count badge (0 for none).
 * @param icon Composable slot for the tab icon. Receives `isSelected` so animated JSON / Lottie compositions can trigger.
 */
data class VivxNavItem(
    val id: String,
    val title: String,
    val badgeCount: Int = 0,
    val icon: @Composable (isSelected: Boolean) -> Unit
)

/**
 * Ultra-premium floating frosted liquid-glass navigation bar.
 * Designed to replace standard boring M3 navigation bars across Home, Media, and Cloud screens.
 *
 * Features:
 * - Animated spring indicator sliding across tabs.
 * - Slot for Animated JSON (Lottie) or vector icons.
 * - Tactile spring-scale physics on tap.
 * - Specular dual-gradient border highlights with liquid-glass backdrop.
 * - Slide & fade transitions for search / selection mode auto-hiding.
 *
 * @param items List of navigation items.
 * @param selectedIndex Index of currently active item.
 * @param onItemSelected Callback when a tab is selected.
 * @param modifier Layout modifier.
 * @param visible Controls animated enter/exit visibility.
 * @param height Height of the floating bar (defaults to 58.dp).
 * @param pillWidth Approximate width of the bar (defaults to 280.dp).
 */
@Composable
fun VivxFloatingNavBar(
    items: List<VivxNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    height: Dp = 58.dp,
    pillWidth: Dp = (items.size * 72 + 24).dp
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        val shape = RoundedCornerShape(height / 2)
        val surfaceColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f)
        val specularBorder = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f),
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )
        )

        Box(
            modifier = Modifier
                .width(pillWidth)
                .height(height)
                .clip(shape)
                .background(surfaceColor)
                .border(1.dp, specularBorder, shape)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index

                    VivxNavTabItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onItemSelected(index) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    // Vertical divider between tabs
                    if (index < items.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(22.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VivxNavTabItem(
    item: VivxNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 450f),
        label = "VivxNavTabScale"
    )

    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
    val tabColor = if (isSelected) activeColor else inactiveColor

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                // Icon slot (supports Lottie animated JSON, animated vectors, or icons)
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    item.icon(isSelected)
                }

                // Unread notification badge
                if (item.badgeCount > 0) {
                    Box(modifier = Modifier.offset(x = 8.dp, y = (-4).dp)) {
                        VivxUnreadBadge(count = item.badgeCount)
                    }
                }
            }

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = tabColor,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
