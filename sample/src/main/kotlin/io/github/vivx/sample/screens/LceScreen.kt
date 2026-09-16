package io.github.vivx.sample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vivx.layout.verticalSpacer
import io.github.vivx.state.VivxContent
import io.github.vivx.state.VivxLce

@Composable
fun LceScreen(modifier: Modifier = Modifier) {
    var lceState by remember {
        mutableStateOf<VivxLce<List<String>>>(
            VivxLce.Content(listOf("Dashboard Analytics", "Recent Invoices", "User Growth", "Server Health"))
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "LCE State Machine",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Switch between states to test declarative transitions",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        verticalSpacer(12.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = lceState is VivxLce.Content,
                onClick = {
                    lceState = VivxLce.Content(listOf("Dashboard Analytics", "Recent Invoices", "User Growth", "Server Health"))
                },
                label = { Text("Content") }
            )
            FilterChip(
                selected = lceState is VivxLce.Loading,
                onClick = { lceState = VivxLce.Loading },
                label = { Text("Loading") }
            )
            FilterChip(
                selected = lceState is VivxLce.Error,
                onClick = { lceState = VivxLce.Error("Failed to fetch dashboard data from server.") },
                label = { Text("Error") }
            )
            FilterChip(
                selected = lceState is VivxLce.Empty,
                onClick = { lceState = VivxLce.Empty },
                label = { Text("Empty") }
            )
        }

        verticalSpacer(16.dp)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            VivxContent(
                state = lceState,
                onRetry = {
                    lceState = VivxLce.Loading
                }
            ) { items ->
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
