package io.github.vivx.sample.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.vivx.components.VivxNumberField
import io.github.vivx.components.VivxSwitchField
import io.github.vivx.components.VivxTextField
import io.github.vivx.dialog.VivxBottomSheetForm
import io.github.vivx.dialog.VivxFormDialog
import io.github.vivx.form.rememberVivxForm
import io.github.vivx.layout.VivxOverlayScaffold
import io.github.vivx.layout.verticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaOverlayDemoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var isMiniPlayerVisible by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var showStreamDialog by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    // Stream URL Form with URL validation
    val streamForm = rememberVivxForm {
        field("streamUrl") {
            required("Stream URL cannot be empty")
            url("Must start with http://, https://, or rtsp://")
        }
    }

    // Player Quick Settings Sheet Form
    val settingsForm = rememberVivxForm {
        field("speed", initialValue = "1.0") {
            numberRange(0.5, 2.0, "Speed must be 0.5x to 2.0x")
        }
        field("hwAccel", initialValue = "true")
        field("backgroundPlay", initialValue = "false")
    }

    VivxOverlayScaffold(
        modifier = modifier,
        isMiniPlayerVisible = isMiniPlayerVisible,
        miniPlayerHeight = 68.dp,
        isFloatingBarVisible = true,
        floatingBarHeight = 54.dp,
        miniPlayer = {
            // NextPlayer-style MiniPlayer bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VideoLibrary, contentDescription = null)
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("BigBuckBunny_1080p.mp4", style = MaterialTheme.typography.titleSmall)
                            Text("Telegram Cloud • 04:15 / 10:30", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Row {
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                        }
                    }
                }
            }
        },
        floatingBottomBar = {
            // Floating pill navigation bar (NextPlayer Telegram style)
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(27.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val tabs = listOf("Library" to Icons.Default.Folder, "Cloud" to Icons.Default.Cloud, "Settings" to Icons.Default.Settings)
                    tabs.forEachIndexed { index, pair ->
                        IconButton(onClick = { selectedNavIndex = index }) {
                            Icon(
                                imageVector = pair.second,
                                contentDescription = pair.first,
                                tint = if (selectedNavIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Media Player Architecture", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Notice how list scrolling automatically clears both the MiniPlayer and Floating Pill without manual padding calculations!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    verticalSpacer(16.dp)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { showStreamDialog = true }) {
                            Icon(Icons.Default.AddLink, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(" Stream URL", modifier = Modifier.padding(start = 4.dp))
                        }
                        Button(onClick = { showSettingsSheet = true }) {
                            Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(" Speed & HW", modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    verticalSpacer(8.dp)

                    Button(onClick = { isMiniPlayerVisible = !isMiniPlayerVisible }) {
                        Text(if (isMiniPlayerVisible) "Hide MiniPlayer" else "Show MiniPlayer")
                    }
                }
            }

            items((1..25).toList()) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Video Item #$index", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }

    // Modal Form Dialog for Stream URL
    if (showStreamDialog) {
        VivxFormDialog(
            form = streamForm,
            onDismissRequest = { showStreamDialog = false },
            title = "Open Network Stream",
            confirmLabel = "Play Stream",
            onConfirm = { data ->
                Toast.makeText(context, "Playing: ${data["streamUrl"]}", Toast.LENGTH_LONG).show()
            }
        ) {
            VivxTextField(
                key = "streamUrl",
                label = "Stream URL",
                placeholder = "https://example.com/live.m3u8"
            )
        }
    }

    // Modal BottomSheet for Playback Settings
    if (showSettingsSheet) {
        VivxBottomSheetForm(
            form = settingsForm,
            onDismissRequest = { showSettingsSheet = false },
            title = "Playback Settings",
            saveLabel = "Apply Settings",
            onSave = { data ->
                Toast.makeText(context, "Speed: ${data["speed"]}x | HW: ${data["hwAccel"]}", Toast.LENGTH_SHORT).show()
            }
        ) {
            VivxNumberField(
                key = "speed",
                label = "Playback Speed",
                step = 0.25,
                min = 0.5,
                max = 2.0,
                isDecimal = true,
                suffix = "x"
            )

            verticalSpacer(12.dp)

            VivxSwitchField(
                key = "hwAccel",
                title = "Hardware Acceleration (HW+)",
                subtitle = "Use hardware decoders for smooth 4K/60fps playback"
            )

            VivxSwitchField(
                key = "backgroundPlay",
                title = "Background Audio Playback",
                subtitle = "Continue playing audio when screen is turned off"
            )
        }
    }
}
