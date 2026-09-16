package io.github.vivx.form

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VivxFormTest {

    @Test
    fun requiredRule_validatesProperly() {
        val rule = RequiredRule("Field is required")
        assertEquals("Field is required", rule.validate(""))
        assertEquals("Field is required", rule.validate("   "))
        assertNull(rule.validate("hello"))
    }

    @Test
    fun emailRule_validatesEmailAddresses() {
        val rule = EmailRule("Invalid email")
        assertNull(rule.validate("user@example.com"))
        assertNull(rule.validate("user.name+tag@sub.domain.co"))
        assertEquals("Invalid email", rule.validate("not-an-email"))
        assertEquals("Invalid email", rule.validate("user@"))
        assertEquals("Invalid email", rule.validate("@domain.com"))
    }

    @Test
    fun minLengthRule_enforcesMinimumLength() {
        val rule = MinLengthRule(8, "Minimum 8 chars")
        assertEquals("Minimum 8 chars", rule.validate("1234567"))
        assertNull(rule.validate("12345678"))
        assertNull(rule.validate("123456789"))
    }

    @Test
    fun formState_handlesValidationAndMatchesProperly() {
        val form = VivxFormBuilder().apply {
            field("email") {
                required("Email required")
                email("Invalid email")
            }
            field("password") {
                required("Password required")
                minLength(6, "Min 6 chars")
            }
            field("confirmPassword") {
                required("Confirm required")
                matches("password", "Passwords do not match")
            }
        }.build()

        // Initially untouched, error is null
        assertNull(form["email"].error)
        assertFalse(form.isValid)

        // Validate all triggers touched
        val validInitial = form.validateAll()
        assertFalse(validInitial)
        assertTrue(form["email"].isTouched)
        assertEquals("Email required", form["email"].error)

        // Enter invalid email
        form["email"].onValueChange("bad-email")
        assertEquals("Invalid email", form["email"].error)

        // Enter valid email
        form["email"].onValueChange("test@example.com")
        assertNull(form["email"].error)

        // Password mismatch
        form["password"].onValueChange("secret123")
        form["confirmPassword"].onValueChange("secret456")
        assertEquals("Passwords do not match", form["confirmPassword"].rawError)

        // Password matches
        form["confirmPassword"].onValueChange("secret123")
        assertNull(form["confirmPassword"].rawError)
        assertTrue(form.isValid)

        // Verify toMap output
        val data = form.toMap()
        assertEquals("test@example.com", data["email"])
        assertEquals("secret123", data["password"])
        assertEquals("secret123", data["confirmPassword"])
    }

    @Test
    fun formNavigation_resolvesFieldOrderAndNextCorrectly() {
        val form = VivxFormBuilder().apply {
            field("first")
            field("second")
            field("third")
        }.build()

        assertEquals("second", form.getNextField("first")?.key)
        assertEquals("third", form.getNextField("second")?.key)
        assertNull(form.getNextField("third"))

        assertFalse(form.isLastField("first"))
        assertFalse(form.isLastField("second"))
        assertTrue(form.isLastField("third"))
    }
}
