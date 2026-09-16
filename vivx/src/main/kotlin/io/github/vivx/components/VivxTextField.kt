package io.github.vivx.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.vivx.form.LocalVivxForm
import io.github.vivx.form.VivxFieldState
import io.github.vivx.form.VivxFormState

/**
 * Intelligent Material 3 text input field hooked to a Vivx form field.
 * Automatically handles:
 * - Auto focus chaining (Next key focuses the next input).
 * - Last field detection (Done key closes keyboard).
 * - Blur detection (marks touched on focus loss).
 * - Animated validation error display.
 *
 * @param key The unique key of the field defined in [rememberVivxForm].
 * @param label Text label for the field.
 * @param placeholder Optional placeholder text.
 * @param modifier Layout modifier for the text field.
 * @param keyboardType Keyboard type (e.g. Email, Text, Number, Phone).
 * @param visualTransformation Transformation (e.g. PasswordVisualTransformation).
 * @param leadingIcon Optional composable icon displayed at the start.
 * @param trailingIcon Optional composable icon displayed at the end.
 * @param singleLine Whether the text field is constrained to a single line.
 * @param onDone Optional callback triggered when the keyboard's Done button is pressed.
 */
@Composable
fun VivxTextField(
    key: String,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    onDone: (() -> Unit)? = null
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxTextField must be placed within a VivxForm composable, or use the fieldState overload.")
    val fieldState = formState[key]

    VivxTextFieldImpl(
        fieldState = fieldState,
        formState = formState,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        keyboardType = keyboardType,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        onDone = onDone
    )
}

/**
 * Overload accepting an explicit [VivxFieldState] when used outside of [LocalVivxForm].
 */
@Composable
fun VivxTextField(
    fieldState: VivxFieldState,
    label: String,
    modifier: Modifier = Modifier,
    formState: VivxFormState? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    onDone: (() -> Unit)? = null
) {
    VivxTextFieldImpl(
        fieldState = fieldState,
        formState = formState,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        keyboardType = keyboardType,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        onDone = onDone
    )
}

@Composable
private fun VivxTextFieldImpl(
    fieldState: VivxFieldState,
    formState: VivxFormState?,
    label: String,
    modifier: Modifier,
    placeholder: String?,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    singleLine: Boolean,
    onDone: (() -> Unit)?
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isLast = formState?.isLastField(fieldState.key) ?: true
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldState.value,
            onValueChange = { fieldState.onValueChange(it) },
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(fieldState.focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && fieldState.isDirty) {
                        fieldState.markAsTouched()
                    }
                },
            isError = fieldState.error != null,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    val nextField = formState?.getNextField(fieldState.key)
                    if (nextField != null) {
                        try {
                            nextField.focusRequester.requestFocus()
                        } catch (_: Exception) {}
                    } else {
                        keyboardController?.hide()
                    }
                },
                onDone = {
                    keyboardController?.hide()
                    fieldState.markAsTouched()
                    onDone?.invoke()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            )
        )

        // Smooth animated error message display
        AnimatedVisibility(
            visible = fieldState.error != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            fieldState.error?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}
