package io.github.vivx.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import io.github.vivx.VivxDsl

/**
 * CompositionLocal providing access to the nearest enclosing [VivxFormState].
 */
val LocalVivxForm = staticCompositionLocalOf<VivxFormState?> { null }

/**
 * Scope provided inside [VivxForm] content blocks.
 */
@VivxDsl
class VivxFormScope(
    val form: VivxFormState,
    private val columnScope: ColumnScope
) : ColumnScope by columnScope

/**
 * Top-level form composable container that wires [VivxFormState] to all child components.
 * Automatically wraps content in a [Column] layout so multiple children don't overlap.
 *
 * @param formState The active form state holder.
 * @param modifier Modifier applied to the outer layout container.
 * @param content Form content scoped with [VivxFormScope].
 */
@Composable
fun VivxForm(
    formState: VivxFormState,
    modifier: Modifier = Modifier,
    content: @Composable VivxFormScope.() -> Unit
) {
    CompositionLocalProvider(LocalVivxForm provides formState) {
        Column(modifier = modifier) {
            val scope = VivxFormScope(form = formState, columnScope = this)
            scope.content()
        }
    }
}
