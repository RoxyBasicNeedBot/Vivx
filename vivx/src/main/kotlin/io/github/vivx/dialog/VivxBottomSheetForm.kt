package io.github.vivx.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vivx.form.VivxForm
import io.github.vivx.form.VivxFormScope
import io.github.vivx.form.VivxFormState

/**
 * Intelligent modal bottom sheet container for forms.
 * Handles virtual keyboard padding (imePadding) and drag gestures automatically.
 *
 * @param form Form state holder.
 * @param onDismissRequest Called when sheet is dismissed.
 * @param title Sheet header title.
 * @param sheetState Optional custom sheet state.
 * @param saveLabel Save action button text (defaults to "Save").
 * @param cancelLabel Cancel action button text (defaults to "Cancel").
 * @param onSave Called with validated form data map upon successful validation.
 * @param content Form inputs scoped in [VivxFormScope].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VivxBottomSheetForm(
    form: VivxFormState,
    onDismissRequest: () -> Unit,
    title: String,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    saveLabel: String = "Save",
    cancelLabel: String = "Cancel",
    onSave: (Map<String, String>) -> Unit,
    content: @Composable VivxFormScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(onClick = onDismissRequest) {
                    Text(cancelLabel)
                }
            }

            VivxForm(
                formState = form,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                content()
            }

            Button(
                onClick = {
                    if (form.validateAll()) {
                        onSave(form.toMap())
                        onDismissRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(saveLabel)
            }
        }
    }
}
