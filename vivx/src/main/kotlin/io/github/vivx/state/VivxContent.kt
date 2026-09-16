package io.github.vivx.state

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Standard LCE (Loading / Content / Error / Empty) state representation.
 */
sealed interface VivxLce<out T> {
    data object Loading : VivxLce<Nothing>
    data class Content<T>(val data: T) : VivxLce<T>
    data class Error(val message: String, val cause: Throwable? = null) : VivxLce<Nothing>
    data object Empty : VivxLce<Nothing>

    val isContent: Boolean get() = this is Content
    val isLoading: Boolean get() = this is Loading
    val isError: Boolean get() = this is Error
    val isEmpty: Boolean get() = this is Empty

    fun contentOrNull(): T? = (this as? Content<T>)?.data
}

/**
 * Declarative state handler for Loading, Content, Error, and Empty screens with animated transitions.
 * Eliminates repetitive `when (state)` boilerplate across ViewModels.
 *
 * @param state The current LCE state.
 * @param modifier Layout modifier.
 * @param onRetry Optional callback for retry actions upon error.
 * @param loading Custom composable slot for loading state (defaults to centered progress spinner).
 * @param empty Custom composable slot for empty state (defaults to clean placeholder).
 * @param error Custom composable slot for error state (defaults to error card with retry button).
 * @param content Composable slot rendering the data content when in [VivxLce.Content] state.
 */
@Composable
fun <T> VivxContent(
    state: VivxLce<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    loading: (@Composable () -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null,
    error: (@Composable (message: String, retry: (() -> Unit)?) -> Unit)? = null,
    content: @Composable (data: T) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = state, label = "VivxContentCrossfade") { targetState ->
            when (targetState) {
                is VivxLce.Loading -> {
                    if (loading != null) {
                        loading()
                    } else {
                        DefaultLoadingView()
                    }
                }
                is VivxLce.Empty -> {
                    if (empty != null) {
                        empty()
                    } else {
                        DefaultEmptyView()
                    }
                }
                is VivxLce.Error -> {
                    if (error != null) {
                        error(targetState.message, onRetry)
                    } else {
                        DefaultErrorView(message = targetState.message, onRetry = onRetry)
                    }
                }
                is VivxLce.Content -> {
                    content(targetState.data)
                }
            }
        }
    }
}

@Composable
private fun DefaultLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DefaultEmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No content available",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DefaultErrorView(
    message: String,
    onRetry: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}
