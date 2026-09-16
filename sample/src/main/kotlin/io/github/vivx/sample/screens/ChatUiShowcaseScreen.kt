package io.github.vivx.sample.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.vivx.layout.verticalSpacer
import io.github.vivx.ui.chat.VivxAttachmentState
import io.github.vivx.ui.chat.VivxChatListItem
import io.github.vivx.ui.chat.VivxDocumentBubble
import io.github.vivx.ui.chat.VivxMessageBubble
import io.github.vivx.ui.chat.VivxMessageStatus
import io.github.vivx.ui.chat.VivxOnlineIndicator
import io.github.vivx.ui.chat.VivxPinnedBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatUiShowcaseScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var attachmentState by remember { mutableStateOf(VivxAttachmentState.IDLE) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Vivx Pinned Message Bar
        VivxPinnedBar(
            title = "Pinned Message",
            snippet = "Important update: Next Player v2.0 with Vivx UI is now live!",
            onClick = {
                Toast.makeText(context, "Jumping to pinned message", Toast.LENGTH_SHORT).show()
            },
            onClose = {
                Toast.makeText(context, "Unpinned", Toast.LENGTH_SHORT).show()
            }
        )

        verticalSpacer(8.dp)

        Text(
            text = "Conversation List Items",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 2. Vivx Chat List Items
        VivxChatListItem(
            title = "Next Player Official Channel",
            subtitle = "New video codec engine with HW+ decoder released",
            time = "11:20 AM",
            isVerified = true,
            isPinned = true,
            unreadCount = 14,
            onClick = {
                Toast.makeText(context, "Clicked Channel", Toast.LENGTH_SHORT).show()
            }
        )

        HorizontalDivider(modifier = Modifier.padding(start = 78.dp), color = DividerDefaults.color.copy(alpha = 0.5f))

        VivxChatListItem(
            title = "Android Dev Community",
            subtitle = "Can we use the Vivx UI DSL inside Compose multiplatform?",
            time = "10:05 AM",
            unreadCount = 3,
            isMuted = true,
            avatar = {
                Box {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("AD", fontWeight = FontWeight.Bold)
                        }
                    }
                    VivxOnlineIndicator(
                        isOnline = true,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            },
            onClick = {}
        )

        HorizontalDivider(modifier = Modifier.padding(start = 78.dp), color = DividerDefaults.color.copy(alpha = 0.5f))

        VivxChatListItem(
            title = "Cloud Storage Bot",
            subtitle = "Video file successfully uploaded to cloud",
            time = "Yesterday",
            isOutgoingLastMessage = true,
            isRead = true,
            onClick = {}
        )

        verticalSpacer(16.dp)

        Text(
            text = "Interactive Chat & Media Bubbles",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 3. Incoming Message with Reply Quote
        VivxMessageBubble(
            text = "Hey brother! Check out this movie file I uploaded to Telegram Cloud.",
            time = "11:32 AM",
            isOutgoing = false,
            replyAuthor = "Next Player Team",
            replySnippet = "Streaming architecture updated with Vivx DSL"
        )

        // 4. File / Video Document Bubble with Interactive Download / Play Progress
        VivxDocumentBubble(
            fileName = "Dune_Part_Two_1080p_HDR.mkv",
            fileSize = "2.4 GB",
            isOutgoing = false,
            state = attachmentState,
            progress = downloadProgress,
            onActionClick = {
                when (attachmentState) {
                    VivxAttachmentState.IDLE -> {
                        coroutineScope.launch {
                            attachmentState = VivxAttachmentState.DOWNLOADING
                            downloadProgress = 0f
                            while (downloadProgress < 1f) {
                                delay(200)
                                downloadProgress += 0.2f
                            }
                            attachmentState = VivxAttachmentState.READY
                        }
                    }
                    VivxAttachmentState.DOWNLOADING -> {
                        attachmentState = VivxAttachmentState.IDLE
                        downloadProgress = 0f
                    }
                    VivxAttachmentState.READY -> {
                        Toast.makeText(context, "Playing 2.4 GB video with ExoPlayer", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onClick = {
                if (attachmentState == VivxAttachmentState.READY) {
                    Toast.makeText(context, "Opening Dune_Part_Two.mkv", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // 5. Outgoing Message with Double-Check Read Receipt
        VivxMessageBubble(
            text = "Thanks! The download speed is blazing fast and the UI looks super slick.",
            time = "11:34 AM",
            isOutgoing = true,
            status = VivxMessageStatus.READ
        )

        verticalSpacer(24.dp)
    }
}
