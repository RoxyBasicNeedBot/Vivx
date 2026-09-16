package io.github.vivx.form

import androidx.compose.runtime.Stable

/**
 * Interface representing a validation rule applied to a form field.
 */
@Stable
interface VivxValidationRule {
    /**
     * Validates the given input string.
     * @param value Current string value of the field.
     * @return null if valid, or an error message string if invalid.
     */
    fun validate(value: String): String?
}

/**
 * Validates that the input is not blank or empty.
 */
class RequiredRule(
    private val errorMessage: String = "This field is required"
) : VivxValidationRule {
    override fun validate(value: String): String? {
        return if (value.trim().isEmpty()) errorMessage else null
    }
}

/**
 * Validates that the input is a valid email address.
 */
class EmailRule(
    private val errorMessage: String = "Please enter a valid email address"
) : VivxValidationRule {
    private val emailRegex = Regex(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    )

    override fun validate(value: String): String? {
        if (value.isEmpty()) return null // Use RequiredRule for mandatory check
        return if (emailRegex.matches(value.trim())) null else errorMessage
    }
}

/**
 * Validates minimum string length.
 */
class MinLengthRule(
    private val minLength: Int,
    private val errorMessage: String = "Minimum $minLength characters required"
) : VivxValidationRule {
    override fun validate(value: String): String? {
        if (value.isEmpty()) return null
        return if (value.length < minLength) errorMessage else null
    }
}

/**
 * Validates maximum string length.
 */
class MaxLengthRule(
    private val maxLength: Int,
    private val errorMessage: String = "Maximum $maxLength characters allowed"
) : VivxValidationRule {
    override fun validate(value: String): String? {
        return if (value.length > maxLength) errorMessage else null
    }
}

/**
 * Validates input against a custom Regular Expression pattern.
 */
class RegexRule(
    private val regex: Regex,
    private val errorMessage: String = "Invalid format"
) : VivxValidationRule {
    override fun validate(value: String): String? {
        if (value.isEmpty()) return null
        return if (regex.matches(value)) null else errorMessage
    }
}

/**
 * Validates that the field value matches another field in the form (e.g., password confirmation).
 */
class MatchesRule(
    val targetFieldKey: String,
    private val errorMessage: String = "Values do not match",
    private val valueProvider: () -> String
) : VivxValidationRule {
    override fun validate(value: String): String? {
        val targetValue = valueProvider()
        return if (value == targetValue) null else errorMessage
    }
}

/**
 * Custom rule defined with an inline lambda predicate.
 */
class CustomRule(
    private val errorMessage: String,
    private val predicate: (String) -> Boolean
) : VivxValidationRule {
    override fun validate(value: String): String? {
        return if (predicate(value)) null else errorMessage
    }
}

/**
 * Validates network streaming or web URL (http, https, rtsp, rtmp, m3u8).
 */
class UrlRule(
    private val errorMessage: String = "Please enter a valid stream or web URL",
    private val allowedSchemes: Set<String> = setOf("http", "https", "rtsp", "rtmp", "m3u8", "udp")
) : VivxValidationRule {
    override fun validate(value: String): String? {
        if (value.trim().isEmpty()) return null
        val lower = value.trim().lowercase()
        val hasScheme = allowedSchemes.any { lower.startsWith("$it://") || lower.startsWith("$it:") }
        return if (hasScheme) null else errorMessage
    }
}

/**
 * Validates that numeric input falls within a given min and max range.
 */
class NumberRangeRule(
    private val min: Double = Double.MIN_VALUE,
    private val max: Double = Double.MAX_VALUE,
    private val errorMessage: String = "Value must be between $min and $max"
) : VivxValidationRule {
    override fun validate(value: String): String? {
        if (value.trim().isEmpty()) return null
        val num = value.toDoubleOrNull() ?: return "Please enter a valid number"
        return if (num in min..max) null else errorMessage
    }
}

