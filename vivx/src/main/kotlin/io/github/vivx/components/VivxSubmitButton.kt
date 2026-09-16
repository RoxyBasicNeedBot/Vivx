package io.github.vivx.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vivx.form.LocalVivxForm

/**
 * Intelligent form submit button.
 * Automatically validates all fields, auto-focuses the first error on invalid submit,
 * and passes the validated data map upon success.
 *
 * @param label Button label text.
 * @param modifier Layout modifier.
 * @param isLoading When true, displays a loading spinner and disables click interactions.
 * @param disableIfInvalid When true, disables the button until all fields are valid.
 *                         When false (default), keeps button active and shows all validation errors upon click.
 * @param onSubmit Callback invoked with the validated field values [Map<String, String>] when the form is valid.
 */
@Composable
fun VivxSubmitButton(
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isLoading: Boolean = false,
    disableIfInvalid: Boolean = false,
    onSubmit: (Map<String, String>) -> Unit
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxSubmitButton must be placed within a VivxForm composable.")

    val isButtonEnabled = !isLoading && (!disableIfInvalid || formState.isValid)

    Button(
        onClick = {
            if (!isLoading) {
                val isFormValid = formState.validateAll()
                if (isFormValid) {
                    onSubmit(formState.toMap())
                }
            }
        },
        enabled = isButtonEnabled,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(label)
        }
    }
}

/**
 * Custom content overload of [VivxSubmitButton] for rich button layouts.
 */
@Composable
fun VivxSubmitButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    isLoading: Boolean = false,
    disableIfInvalid: Boolean = false,
    onSubmit: (Map<String, String>) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxSubmitButton must be placed within a VivxForm composable.")

    val isButtonEnabled = !isLoading && (!disableIfInvalid || formState.isValid)

    Button(
        onClick = {
            if (!isLoading) {
                val isFormValid = formState.validateAll()
                if (isFormValid) {
                    onSubmit(formState.toMap())
                }
            }
        },
        enabled = isButtonEnabled,
        modifier = modifier,
        content = content
    )
}
