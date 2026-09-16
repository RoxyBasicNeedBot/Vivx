package io.github.vivx.sample.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.vivx.components.VivxPasswordField
import io.github.vivx.components.VivxSubmitButton
import io.github.vivx.components.VivxTextField
import io.github.vivx.form.VivxForm
import io.github.vivx.form.rememberVivxForm
import io.github.vivx.layout.verticalSpacer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val form = rememberVivxForm {
        field("fullName") {
            required("Full name is required")
            minLength(3, "Name must be at least 3 characters")
        }
        field("email") {
            required("Email is required")
            email("Please enter a valid email")
        }
        field("phone") {
            required("Phone number is required")
            regex(Regex("^[0-9]{10}$"), "Enter a valid 10-digit phone number")
        }
        field("password") {
            required("Password is required")
            minLength(8, "Password must be at least 8 characters")
        }
        field("confirmPassword") {
            required("Please confirm your password")
            matches("password", "Passwords do not match")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            VivxForm(
                formState = form,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Try jumping with Next key on keyboard. Notice password match validation!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                verticalSpacer(16.dp)

                VivxTextField(
                    key = "fullName",
                    label = "Full Name",
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                verticalSpacer(12.dp)

                VivxTextField(
                    key = "email",
                    label = "Email Address",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
                )

                verticalSpacer(12.dp)

                VivxTextField(
                    key = "phone",
                    label = "Phone (10 digits)",
                    keyboardType = KeyboardType.Phone,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                )

                verticalSpacer(12.dp)

                VivxPasswordField(
                    key = "password",
                    label = "Password",
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
                )

                verticalSpacer(12.dp)

                VivxPasswordField(
                    key = "confirmPassword",
                    label = "Confirm Password",
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
                )

                verticalSpacer(24.dp)

                VivxSubmitButton(
                    label = "Register",
                    isLoading = isLoading
                ) { data ->
                    coroutineScope.launch {
                        isLoading = true
                        delay(1000)
                        isLoading = false

                        // Check demo server-side conflict
                        if (data["email"]?.contains("admin") == true) {
                            form.setFieldError("email", "Admin email cannot be registered here")
                        } else {
                            Toast.makeText(
                                context,
                                "Registered successfully: ${data["fullName"]}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
