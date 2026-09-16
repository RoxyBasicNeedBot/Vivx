package io.github.vivx.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Download state of a file attachment.
 */
enum class VivxAttachmentState {
    IDLE,
    DOWNLOADING,
    READY
}

/**
 * File / Video document attachment bubble (Telegram style).
 * Features an interactive circular action button (download, progress cancel, play/open).
 *
 * @param fileName Name of the file or video (e.g. "Dune_Part_Two_1080p.mkv").
 * @param fileSize Formatted size string (e.g. "1.85 GB").
 * @param isOutgoing True if sent by current user.
 * @param state Download status (IDLE, DOWNLOADING, READY).
 * @param progress Download progress between 0f and 1f when DOWNLOADING.
 * @param onActionClick Callback when circular button is pressed.
 * @param modifier Layout modifier.
 */
@Composable
fun VivxDocumentBubble(
    fileName: String,
    fileSize: String,
    isOutgoing: Boolean,
    state: VivxAttachmentState,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    onClick: (() -> Unit)? = null
) {
    val bubbleShape = if (isOutgoing) {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp)
    }

    val bubbleColor = if (isOutgoing) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = bubbleShape,
            color = bubbleColor,
            modifier = Modifier
                .widthIn(max = 320.dp)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Action Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(onClick = onActionClick),
                    contentAlignment = Alignment.Center
                ) {
                    when (state) {
                        VivxAttachmentState.IDLE -> {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        VivxAttachmentState.DOWNLOADING -> {
                            CircularProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.size(42.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 3.dp
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        VivxAttachmentState.READY -> {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }

                // File Details
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(
                        text = fileName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = when (state) {
                            VivxAttachmentState.DOWNLOADING -> "${(progress * 100).toInt()}% • $fileSize"
                            else -> fileSize
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
