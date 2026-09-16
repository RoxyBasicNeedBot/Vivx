package io.github.vivx.form

import androidx.compose.runtime.Stable

/**
 * State holder for an entire form comprising multiple [VivxFieldState] instances.
 */
@Stable
class VivxFormState(
    internal val fields: Map<String, VivxFieldState>,
    internal val fieldOrder: List<String>
) {
    /**
     * Access a specific field state by its unique key.
     */
    operator fun get(key: String): VivxFieldState {
        return fields[key]
            ?: throw IllegalArgumentException("Field '$key' is not registered in this form. Registered: ${fields.keys}")
    }

    /**
     * Returns the string value of a field, or an empty string if not found.
     */
    fun getValue(key: String): String {
        return fields[key]?.value.orEmpty()
    }

    /**
     * True if all fields in the form pass their validation rules.
     */
    val isValid: Boolean
        get() = fields.values.all { it.isValid }

    /**
     * True if any field in the form has been modified by the user.
     */
    val isDirty: Boolean
        get() = fields.values.any { it.isDirty }

    /**
     * True if any field has been marked as touched.
     */
    val isTouched: Boolean
        get() = fields.values.any { it.isTouched }

    /**
     * Validates all fields in the form, marking them as touched.
     * If any field is invalid, automatically requests focus on the FIRST invalid field.
     *
     * @return True if the entire form is valid, false otherwise.
     */
    fun validateAll(): Boolean {
        var firstInvalidField: VivxFieldState? = null

        for (key in fieldOrder) {
            val field = fields[key] ?: continue
            field.markAsTouched()
            if (!field.isValid && firstInvalidField == null) {
                firstInvalidField = field
            }
        }

        // Auto-focus the first invalid field to provide immediate UX feedback
        firstInvalidField?.let {
            try {
                it.focusRequester.requestFocus()
            } catch (_: Exception) {
                // Ignore if focus requester is temporarily detached
            }
        }

        return isValid
    }

    /**
     * Sets a server-side or external error on a specific field and focuses it.
     */
    fun setFieldError(key: String, errorMessage: String, autoFocus: Boolean = true) {
        val field = fields[key] ?: return
        field.setError(errorMessage)
        if (autoFocus) {
            try {
                field.focusRequester.requestFocus()
            } catch (_: Exception) {}
        }
    }

    /**
     * Returns the next field according to the declared field order, or null if it's the last field.
     */
    fun getNextField(currentKey: String): VivxFieldState? {
        val currentIndex = fieldOrder.indexOf(currentKey)
        if (currentIndex in 0 until fieldOrder.size - 1) {
            val nextKey = fieldOrder[currentIndex + 1]
            return fields[nextKey]
        }
        return null
    }

    /**
     * Returns whether the given key belongs to the final field in the form.
     */
    fun isLastField(currentKey: String): Boolean {
        return fieldOrder.lastOrNull() == currentKey
    }

    /**
     * Extracts all field values into an immutable Map of [Key -> Value].
     */
    fun toMap(): Map<String, String> {
        return fields.mapValues { it.value.value }
    }

    /**
     * Resets all fields in the form to their initial values.
     */
    fun reset() {
        fields.values.forEach { it.reset() }
    }
}
