package io.github.vivx.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Message delivery and read status.
 */
enum class VivxMessageStatus {
    SENDING,
    SENT,
    READ
}

/**
 * Modern chat bubble supporting outgoing & incoming messages, reply quote headers,
 * timestamps, and double-check read receipts.
 *
 * @param text Message body text.
 * @param time Timestamp string (e.g. "10:45 AM").
 * @param isOutgoing True if sent by the current user, false if incoming.
 * @param modifier Layout modifier.
 * @param status Delivery status for outgoing messages (SENDING, SENT, READ).
 * @param replyAuthor Optional name of author being replied to.
 * @param replySnippet Optional snippet of quoted reply message.
 * @param onClick Optional tap callback.
 * @param onLongClick Optional long press callback (for context menus / actions).
 */
@Composable
fun VivxMessageBubble(
    text: String,
    time: String,
    isOutgoing: Boolean,
    modifier: Modifier = Modifier,
    status: VivxMessageStatus = VivxMessageStatus.READ,
    replyAuthor: String? = null,
    replySnippet: String? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
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

    val contentColor = if (isOutgoing) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = bubbleShape,
            color = bubbleColor,
            contentColor = contentColor,
            modifier = Modifier
                .widthIn(max = 310.dp)
                .then(
                    if (onClick != null || onLongClick != null) {
                        Modifier.combinedClickable(
                            onClick = { onClick?.invoke() },
                            onLongClick = { onLongClick?.invoke() }
                        )
                    } else Modifier
                )
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                // Reply Quote Banner
                if (replyAuthor != null && replySnippet != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 3.dp, height = 32.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Column(modifier = Modifier.padding(start = 6.dp)) {
                            Text(
                                text = replyAuthor,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = replySnippet,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = contentColor.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Message Text
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = if (replyAuthor != null) 4.dp else 0.dp)
                )

                // Timestamp & Status Indicator
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.65f)
                    )

                    if (isOutgoing) {
                        val statusIcon = when (status) {
                            VivxMessageStatus.SENDING -> Icons.Default.Schedule
                            VivxMessageStatus.SENT -> Icons.Default.Done
                            VivxMessageStatus.READ -> Icons.Default.DoneAll
                        }
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = status.name,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(14.dp),
                            tint = if (status == VivxMessageStatus.READ) MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.65f)
                        )
                    }
                }
            }
        }
    }
}
