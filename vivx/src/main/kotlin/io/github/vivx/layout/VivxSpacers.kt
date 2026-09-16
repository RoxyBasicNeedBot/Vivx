package io.github.vivx.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Scope-aware vertical spacer for [ColumnScope].
 * Only modifies vertical height, avoiding square constraint side-effects.
 */
@Composable
fun ColumnScope.verticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

/**
 * Scope-aware weighted vertical spacer for [ColumnScope].
 */
@Composable
fun ColumnScope.verticalSpacer(weight: Float, fill: Boolean = true) {
    Spacer(modifier = Modifier.weight(weight, fill))
}

/**
 * Scope-aware horizontal spacer for [RowScope].
 * Only modifies horizontal width, avoiding square constraint side-effects.
 */
@Composable
fun RowScope.horizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

/**
 * Scope-aware weighted horizontal spacer for [RowScope].
 */
@Composable
fun RowScope.horizontalSpacer(weight: Float, fill: Boolean = true) {
    Spacer(modifier = Modifier.weight(weight, fill))
}
