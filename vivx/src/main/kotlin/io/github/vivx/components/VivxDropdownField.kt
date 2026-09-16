package io.github.vivx.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vivx.form.LocalVivxForm

/**
 * Dropdown selector hooked to a Vivx form field.
 * Essential for decoder selection (HW / SW), aspect ratio, subtitle encoding, etc.
 *
 * @param key The unique key in [rememberVivxForm].
 * @param label Text label.
 * @param options List of selectable items.
 * @param modifier Layout modifier.
 * @param displayLabel Transform item to user-facing string.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> VivxDropdownField(
    key: String,
    label: String,
    options: List<T>,
    modifier: Modifier = Modifier,
    displayLabel: (T) -> String = { it.toString() }
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxDropdownField must be placed within a VivxForm composable.")
    val fieldState = formState[key]

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = fieldState.value,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = fieldState.error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    val text = displayLabel(item)
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = {
                            fieldState.onValueChange(text)
                            fieldState.markAsTouched()
                            expanded = false
                        }
                    )
                }
            }
        }

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
