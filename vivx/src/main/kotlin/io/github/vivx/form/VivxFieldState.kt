package io.github.vivx.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

/**
 * State holder for an individual form field.
 */
class VivxFieldState(
    val key: String,
    val initialValue: String = "",
    val rules: List<VivxValidationRule> = emptyList()
) {
    /**
     * The current text input value.
     */
    var value by mutableStateOf(initialValue)

    /**
     * Indicates whether the field has lost focus or a submission was attempted.
     */
    var isTouched by mutableStateOf(false)

    /**
     * Indicates whether the user has modified the value from its initial state.
     */
    var isDirty by mutableStateOf(false)

    /**
     * Server-side or external custom error message.
     */
    var customError by mutableStateOf<String?>(null)

    /**
     * Compose focus requester associated with this field for programmatic focus control.
     */
    val focusRequester: FocusRequester = FocusRequester()

    /**
     * Evaluates all validation rules unconditionally and returns the first error message, or null if valid.
     */
    val rawError: String?
        get() {
            if (customError != null) return customError
            for (rule in rules) {
                val err = rule.validate(value)
                if (err != null) return err
            }
            return null
        }

    /**
     * The visible error message. Only shown to the user if the field is touched or dirty.
     */
    val error: String?
        get() = if (isTouched || (isDirty && value.isNotEmpty())) rawError else null

    /**
     * True if the field currently passes all validation rules.
     */
    val isValid: Boolean
        get() = rawError == null

    /**
     * Updates the field value in response to user input.
     */
    fun onValueChange(newValue: String) {
        value = newValue
        isDirty = true
        customError = null // Clear external error on edit
    }

    /**
     * Marks the field as touched (e.g. on blur or on form submission).
     */
    fun markAsTouched() {
        isTouched = true
    }

    /**
     * Manually sets a custom error (e.g. from an API response).
     */
    fun setError(errorMessage: String) {
        customError = errorMessage
        isTouched = true
    }

    /**
     * Clears any custom error.
     */
    fun clearError() {
        customError = null
    }

    /**
     * Resets the field to its initial state.
     */
    fun reset(newValue: String = initialValue) {
        value = newValue
        isTouched = false
        isDirty = false
        customError = null
    }
}
