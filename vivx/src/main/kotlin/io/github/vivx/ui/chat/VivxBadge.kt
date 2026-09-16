package io.github.vivx.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Verified badge icon (used next to verified channel or user names).
 */
@Composable
fun VivxVerifiedBadge(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    badgeColor: Color = MaterialTheme.colorScheme.primary,
    checkColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified",
            tint = checkColor,
            modifier = Modifier.size(size * 0.7f)
        )
    }
}

/**
 * Unread message counter badge pill.
 */
@Composable
fun VivxUnreadBadge(
    count: Int,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false
) {
    if (count <= 0) return

    val displayText = if (count > 999) "999+" else count.toString()
    val bgColor = if (isMuted) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }
    val textColor = if (isMuted) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

/**
 * Online presence indicator dot for user avatars.
 */
@Composable
fun VivxOnlineIndicator(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 12.dp,
    onlineColor: Color = Color(0xFF4CAF50),
    borderColor: Color = MaterialTheme.colorScheme.surface
) {
    if (!isOnline) return

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(onlineColor)
            .border(2.dp, borderColor, CircleShape)
    )
}
