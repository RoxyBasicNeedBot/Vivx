package io.github.vivx.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.vivx.form.LocalVivxForm
import java.util.Locale

/**
 * Intelligent numeric input field with +/- step stepper controls and validation.
 * Perfect for playback speed, subtitle delay/size, cache sizes, and timer settings.
 *
 * @param key The unique key of the numeric field in [rememberVivxForm].
 * @param label Text label for the field.
 * @param modifier Layout modifier.
 * @param step The delta to add/subtract per stepper click.
 * @param min Minimum allowed value.
 * @param max Maximum allowed value.
 * @param isDecimal Whether decimal values are permitted.
 * @param suffix Optional suffix text (e.g. "ms", "x", "MB", "s").
 */
@Composable
fun VivxNumberField(
    key: String,
    label: String,
    modifier: Modifier = Modifier,
    step: Double = 1.0,
    min: Double = Double.NEGATIVE_INFINITY,
    max: Double = Double.POSITIVE_INFINITY,
    isDecimal: Boolean = false,
    suffix: String? = null
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxNumberField must be placed within a VivxForm composable.")
    val fieldState = formState[key]

    val currentValue = fieldState.value.toDoubleOrNull() ?: 0.0

    fun updateSteppedValue(delta: Double) {
        val next = (currentValue + delta).coerceIn(min, max)
        val formatted = if (isDecimal) {
            String.format(Locale.US, "%.2f", next).trimEnd('0').trimEnd('.')
        } else {
            next.toLong().toString()
        }
        fieldState.onValueChange(formatted)
        fieldState.markAsTouched()
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldState.value,
            onValueChange = { newValue ->
                val filtered = if (isDecimal) {
                    newValue.filter { it.isDigit() || it == '.' || it == '-' }
                } else {
                    newValue.filter { it.isDigit() || it == '-' }
                }
                fieldState.onValueChange(filtered)
            },
            label = { Text(label) },
            suffix = suffix?.let { { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(fieldState.focusRequester),
            isError = fieldState.error != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number
            ),
            leadingIcon = {
                IconButton(
                    onClick = { updateSteppedValue(-step) },
                    enabled = currentValue - step >= min
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = { updateSteppedValue(step) },
                    enabled = currentValue + step <= max
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            )
        )

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
