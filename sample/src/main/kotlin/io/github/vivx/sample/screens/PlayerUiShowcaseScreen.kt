package io.github.vivx.sample.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.vivx.layout.verticalSpacer
import io.github.vivx.ui.ambient.VivxAmbientGlow
import io.github.vivx.ui.animation.VivxExpandableText
import io.github.vivx.ui.animation.vivxTactileClick
import io.github.vivx.ui.glass.VivxGlassCard
import io.github.vivx.ui.player.VivxPlayerControls
import io.github.vivx.ui.player.VivxPlayerSlider
import io.github.vivx.ui.shimmer.VivxShimmerBox

@Composable
fun PlayerUiShowcaseScreen(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeekMs by remember { mutableLongStateOf(75000L) }
    val totalDurationMs = 234000L // 3:54

    var isFavorite by remember { mutableStateOf(false) }
    var showShimmerSkeleton by remember { mutableStateOf(false) }

    val activeAmbientTone = Color(0xFF6750A4)

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Vivx Ambient Glow (Atmospheric lighting breathing across top of screen)
        VivxAmbientGlow(tint = activeAmbientTone, height = 320.dp, alpha = 0.5f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Now Playing", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Shimmer Demo", style = MaterialTheme.typography.labelSmall)
                    Switch(
                        checked = showShimmerSkeleton,
                        onCheckedChange = { showShimmerSkeleton = it },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            verticalSpacer(20.dp)

            if (showShimmerSkeleton) {
                // 2. Vivx Shimmer Loading Skeleton
                VivxShimmerBox(
                    modifier = Modifier.size(280.dp),
                    shape = RoundedCornerShape(24.dp)
                )
                verticalSpacer(24.dp)
                VivxShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.6f).height(24.dp),
                    shape = RoundedCornerShape(8.dp)
                )
                verticalSpacer(8.dp)
                VivxShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.4f).height(16.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            } else {
                // 3. Vivx Frosted Liquid Glass Artwork Card with Tactile Click
                VivxGlassCard(
                    modifier = Modifier
                        .size(280.dp)
                        .vivxTactileClick(scaleDown = 0.95f),
                    shape = RoundedCornerShape(28.dp),
                    backgroundAlpha = 0.82f
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                verticalSpacer(28.dp)

                // Track Title & Favorite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Starry Night Waves",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Vivx Audio • High Fidelity Lossless",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.vivxTactileClick()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            verticalSpacer(20.dp)

            // 4. Vivx Player Slider Scrubber
            VivxPlayerSlider(
                currentPositionMs = currentSeekMs,
                totalDurationMs = totalDurationMs,
                onSeek = { currentSeekMs = it }
            )

            verticalSpacer(16.dp)

            // 5. Vivx Player Controls with Tactile Play/Pause Spring
            VivxPlayerControls(
                isPlaying = isPlaying,
                onPlayPause = { isPlaying = !isPlaying },
                onPrevious = { currentSeekMs = 0L },
                onNext = { currentSeekMs = 0L },
                onShuffle = {},
                onRepeat = {}
            )

            verticalSpacer(24.dp)

            // 6. Vivx Expandable Text for Lyrics / Description
            VivxGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                backgroundAlpha = 0.65f
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Track Notes & Lyrics", style = MaterialTheme.typography.titleSmall)
                    verticalSpacer(8.dp)
                    VivxExpandableText(
                        text = "Recorded in lossless 24-bit 96kHz format. Designed with fluid tactile UI primitives, " +
                                "featuring liquid-glass refraction, ambient atmospheric top glows, and responsive spring animations. " +
                                "Tap this text card to toggle full caption details.",
                        collapsedMaxLines = 2
                    )
                }
            }
        }
    }
}
