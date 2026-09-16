package io.github.vivx.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Specialized media-app scaffold that solves overlapping floating elements:
 * - Dynamic bottom content padding that automatically accounts for MiniPlayer + Floating Nav Bar.
 * - Auto-hiding floating nav bar during search or selection modes.
 * - Zero clipping or manual offset bugs.
 *
 * @param modifier Layout modifier.
 * @param miniPlayer Composable slot for the bottom mini-player bar.
 * @param miniPlayerHeight Height allocated to the mini-player when visible.
 * @param isMiniPlayerVisible Whether the mini-player is currently visible.
 * @param floatingBottomBar Composable slot for floating navigation pills or actions.
 * @param isFloatingBarVisible Whether the floating navigation bar should be shown.
 * @param floatingBarHeight Approximate height of the floating bar.
 * @param floatingBarBottomMargin Spacing between floating bar and mini-player / navigation bar.
 * @param topBar Optional top app bar slot.
 * @param content Main screen content receiving calculated [PaddingValues].
 */
@Composable
fun VivxOverlayScaffold(
    modifier: Modifier = Modifier,
    miniPlayer: (@Composable () -> Unit)? = null,
    miniPlayerHeight: Dp = 72.dp,
    isMiniPlayerVisible: Boolean = false,
    floatingBottomBar: (@Composable () -> Unit)? = null,
    isFloatingBarVisible: Boolean = true,
    floatingBarHeight: Dp = 56.dp,
    floatingBarBottomMargin: Dp = 16.dp,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    val navInsets = WindowInsets.navigationBars.asPaddingValues()

    // Dynamically calculate bottom padding required for list/grid content
    val calculatedBottomPadding = navInsets.calculateBottomPadding() +
            (if (isMiniPlayerVisible && miniPlayer != null) miniPlayerHeight else 0.dp) +
            (if (isFloatingBarVisible && floatingBottomBar != null) floatingBarHeight + (floatingBarBottomMargin * 2) else 0.dp)

    val contentPadding = PaddingValues(
        start = navInsets.calculateStartPadding(layoutDirection),
        end = navInsets.calculateEndPadding(layoutDirection),
        top = 0.dp,
        bottom = calculatedBottomPadding
    )

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Base content receiving safe dynamic bottom padding
        content(contentPadding)

        // 2. Optional Top Bar
        if (topBar != null) {
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
                topBar()
            }
        }

        // 3. MiniPlayer (Anchored directly above system navigation bar)
        if (miniPlayer != null && isMiniPlayerVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = navInsets.calculateBottomPadding())
            ) {
                miniPlayer()
            }
        }

        // 4. Floating Nav Bar / Pill (Floating cleanly above the MiniPlayer)
        if (floatingBottomBar != null) {
            val pillBottomOffset = navInsets.calculateBottomPadding() +
                    (if (isMiniPlayerVisible && miniPlayer != null) miniPlayerHeight else 0.dp) +
                    floatingBarBottomMargin

            AnimatedVisibility(
                visible = isFloatingBarVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = pillBottomOffset)
            ) {
                floatingBottomBar()
            }
        }
    }
}
