package io.github.vivx.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.vivx.VivxDsl

/**
 * DSL builder for configuring validation rules on a single form field.
 */
@VivxDsl
class VivxFieldBuilder(val key: String) {
    internal val rules = mutableListOf<VivxValidationRule>()

    /**
     * Enforces that the field cannot be blank or empty.
     */
    fun required(errorMessage: String = "This field is required") {
        rules.add(RequiredRule(errorMessage))
    }

    /**
     * Enforces valid email structure.
     */
    fun email(errorMessage: String = "Please enter a valid email address") {
        rules.add(EmailRule(errorMessage))
    }

    /**
     * Enforces minimum character count.
     */
    fun minLength(length: Int, errorMessage: String = "Minimum $length characters required") {
        rules.add(MinLengthRule(length, errorMessage))
    }

    /**
     * Enforces maximum character count.
     */
    fun maxLength(length: Int, errorMessage: String = "Maximum $length characters allowed") {
        rules.add(MaxLengthRule(length, errorMessage))
    }

    /**
     * Enforces a Regular Expression match.
     */
    fun regex(pattern: Regex, errorMessage: String = "Invalid format") {
        rules.add(RegexRule(pattern, errorMessage))
    }

    /**
     * Enforces matching with another field in this form (e.g. Password Confirmation).
     */
    fun matches(otherFieldKey: String, errorMessage: String = "Values do not match") {
        rules.add(DeferredMatchesRule(otherFieldKey, errorMessage))
    }

    /**
     * Custom validation rule using a lambda predicate.
     */
    fun custom(errorMessage: String, predicate: (String) -> Boolean) {
        rules.add(CustomRule(errorMessage, predicate))
    }
}

/**
 * Internal deferred rule used to wire inter-field dependencies once the form is built.
 */
internal class DeferredMatchesRule(
    val targetKey: String,
    val errorMessage: String
) : VivxValidationRule {
    var resolvedRule: MatchesRule? = null

    override fun validate(value: String): String? {
        return resolvedRule?.validate(value)
    }
}

/**
 * Internal representation of a field definition before building [VivxFormState].
 */
internal data class FieldDefinition(
    val key: String,
    val initialValue: String,
    val builder: VivxFieldBuilder
)

/**
 * DSL builder for constructing a [VivxFormState].
 */
@VivxDsl
class VivxFormBuilder {
    internal val definitions = mutableListOf<FieldDefinition>()

    /**
     * Registers a new field within the form.
     */
    fun field(
        key: String,
        initialValue: String = "",
        config: VivxFieldBuilder.() -> Unit = {}
    ) {
        val builder = VivxFieldBuilder(key).apply(config)
        definitions.add(FieldDefinition(key, initialValue, builder))
    }

    internal fun build(): VivxFormState {
        val fieldMap = mutableMapOf<String, VivxFieldState>()
        val order = definitions.map { it.key }

        for (def in definitions) {
            val state = VivxFieldState(
                key = def.key,
                initialValue = def.initialValue,
                rules = def.builder.rules
            )
            fieldMap[def.key] = state
        }

        val formState = VivxFormState(fieldMap, order)

        // Resolve any deferred inter-field matches rules
        for (fieldState in fieldMap.values) {
            for (rule in fieldState.rules) {
                if (rule is DeferredMatchesRule) {
                    val targetField = fieldMap[rule.targetKey]
                        ?: throw IllegalArgumentException("Field '${rule.targetKey}' referenced in matches() does not exist.")
                    rule.resolvedRule = MatchesRule(
                        targetFieldKey = rule.targetKey,
                        errorMessage = rule.errorMessage,
                        valueProvider = { targetField.value }
                    )
                }
            }
        }

        return formState
    }
}

/**
 * Remembers and creates a [VivxFormState] instance preserving state across recompositions.
 */
@Composable
fun rememberVivxForm(builder: VivxFormBuilder.() -> Unit): VivxFormState {
    return remember {
        VivxFormBuilder().apply(builder).build()
    }
}
