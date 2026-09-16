package io.github.vivx.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.vivx.form.VivxForm
import io.github.vivx.form.VivxFormScope
import io.github.vivx.form.VivxFormState

/**
 * Intelligent modal dialog wrapper for forms.
 * Perfect for stream URL inputs, file renames, audio offsets, and quick prompts.
 *
 * @param form Form state holder.
 * @param onDismissRequest Called when user dismisses the dialog.
 * @param title Dialog title text.
 * @param confirmLabel Confirm button text (defaults to "OK").
 * @param dismissLabel Dismiss button text (defaults to "Cancel").
 * @param onConfirm Called with validated form data map when the user submits a valid form.
 * @param content Form content block.
 */
@Composable
fun VivxFormDialog(
    form: VivxFormState,
    onDismissRequest: () -> Unit,
    title: String,
    confirmLabel: String = "OK",
    dismissLabel: String = "Cancel",
    onConfirm: (Map<String, String>) -> Unit,
    content: @Composable VivxFormScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            VivxForm(formState = form, modifier = Modifier.fillMaxWidth()) {
                content()
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (form.validateAll()) {
                        onConfirm(form.toMap())
                        onDismissRequest()
                    }
                }
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissLabel)
            }
        }
    )
}
