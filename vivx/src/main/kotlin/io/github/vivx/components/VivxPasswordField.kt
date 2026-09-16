package io.github.vivx.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Specialized text field for passwords with built-in show/hide visibility toggle.
 *
 * @param key The unique key of the password field in the form.
 * @param label Text label for the field (defaults to "Password").
 * @param modifier Layout modifier.
 * @param onDone Optional callback triggered when Done IME action is pressed.
 */
@Composable
fun VivxPasswordField(
    key: String,
    label: String = "Password",
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    VivxTextField(
        key = key,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = leadingIcon,
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        singleLine = true,
        onDone = onDone
    )
}
