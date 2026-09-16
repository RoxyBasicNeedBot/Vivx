package io.github.vivx.sample.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    // 1. Declare form schema with concise validation DSL
    val form = rememberVivxForm {
        field("email") {
            required("Email is required")
            email("Enter a valid email address")
        }
        field("password") {
            required("Password is required")
            minLength(6, "Password must be at least 6 characters")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            // 2. Wire form state into VivxForm container
            VivxForm(
                formState = form,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Sign in to test automated focus chaining & validation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                verticalSpacer(20.dp)

                // 3. Declarative inputs with auto focus progression
                VivxTextField(
                    key = "email",
                    label = "Email Address",
                    placeholder = "name@company.com",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                    }
                )

                verticalSpacer(12.dp)

                VivxPasswordField(
                    key = "password",
                    label = "Password",
                    placeholder = "••••••••",
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
                    }
                )

                verticalSpacer(24.dp)

                // 4. Intelligent submit button with auto-validation
                VivxSubmitButton(
                    label = "Sign In",
                    isLoading = isLoading
                ) { data ->
                    coroutineScope.launch {
                        isLoading = true
                        delay(1200) // Simulate network call
                        isLoading = false
                        Toast.makeText(
                            context,
                            "Login Successful: ${data["email"]}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
