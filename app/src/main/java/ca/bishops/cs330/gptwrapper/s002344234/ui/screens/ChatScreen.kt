package ca.bishops.cs330.gptwrapper.s002344234.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.graphics.Color
import ca.bishops.cs330.gptwrapper.s002344234.model.Message
import ca.bishops.cs330.gptwrapper.s002344234.ui.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp) // existing padding
            .padding(top = 24.dp) // extra top padding so it’s not flush with the top
    ) {
        ///Tone selector
        var toneExpanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Button(onClick = { toneExpanded = true }) {
                Text("Tone: ${uiState.tone}")
            }
            DropdownMenu(
                expanded = toneExpanded,
                onDismissRequest = { toneExpanded = false }
            ) {
                listOf("Default", "Professional", "Casual", "Humorous").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.setTone(option)
                            toneExpanded = false
                        }
                    )
                }
            }
        }

        ///Text size selector
        var sizeExpanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Button(onClick = { sizeExpanded = true }) {
                val sizeLabel = when (uiState.textSize.value) {
                    in 0f..14f -> "Small"
                    in 14f..18f -> "Medium"
                    else -> "Large"
                }
                Text("Text size: $sizeLabel")
            }
            DropdownMenu(
                expanded = sizeExpanded,
                onDismissRequest = { sizeExpanded = false }
            ) {
                listOf(12f to "Small", 16f to "Medium", 20f to "Large").forEach { (size, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            viewModel.setTextSize(size)
                            sizeExpanded = false
                        }
                    )
                }
            }
        }

        ///Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(uiState.messages) { msg ->
                MessageBubble(
                    msg = msg,
                    textSize = uiState.textSize,
                    onAction = { action ->
                        viewModel.sendQuickAction(action, msg.text)
                    }
                )
            }
        }

        // Auto-scroll to the bottom when a new message is added
        LaunchedEffect(uiState.messages.size) {
            if (uiState.messages.isNotEmpty()) {
                coroutineScope.launch {
                    listState.animateScrollToItem(uiState.messages.lastIndex)
                }
            }
        }

        ///Input row
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp) // extra bottom padding
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = uiState.input,
                onValueChange = { viewModel.setInput(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") },
                singleLine = true
            )
            Button(
                onClick = { viewModel.send() },
                enabled = !uiState.sending
            ) {
                Text("Send")
            }
        }
    }
}

///creating bubbles for messages
@Composable
fun MessageBubble(
    msg: Message,
    textSize: TextUnit,
    onAction: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (msg.isUser) Color(0xFFD0E8FF) else Color(0xFFEFEFEF))
                .padding(12.dp)
        ) {
            Text(msg.text, fontSize = textSize)
        }


        ///hints, explain, translate buttons
        if (!msg.isUser) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = { onAction("hint") }) { Text("Hint") }
                TextButton(onClick = { onAction("explain") }) { Text("Explain Again") }
                TextButton(onClick = { onAction("translate") }) { Text("Translate") }
            }
        }
    }
}
