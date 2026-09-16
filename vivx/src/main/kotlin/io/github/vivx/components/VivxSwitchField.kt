package io.github.vivx.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vivx.form.LocalVivxForm

/**
 * Switch toggle row hooked to a Vivx form field.
 * Handles boolean form data ("true" / "false") with accessible tap row.
 *
 * @param key The unique key in [rememberVivxForm].
 * @param title Primary title.
 * @param subtitle Optional subtitle description.
 * @param modifier Layout modifier.
 */
@Composable
fun VivxSwitchField(
    key: String,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val formState = LocalVivxForm.current
        ?: throw IllegalStateException("VivxSwitchField must be placed within a VivxForm composable.")
    val fieldState = formState[key]

    val isChecked = fieldState.value.equals("true", ignoreCase = true)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val next = (!isChecked).toString()
                fieldState.onValueChange(next)
                fieldState.markAsTouched()
            }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = { checked ->
                fieldState.onValueChange(checked.toString())
                fieldState.markAsTouched()
            }
        )
    }
}
