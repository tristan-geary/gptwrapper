package ca.bishops.cs330.gptwrapper.s002344234.ui

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.bishops.cs330.gptwrapper.s002344234.data.AppRepository
import ca.bishops.cs330.gptwrapper.s002344234.network.Message
import ca.bishops.cs330.gptwrapper.s002344234.model.Message as UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

///UI state for chat screen
data class UiState(
    val messages: List<UiMessage> = emptyList(),
    val input: String = "",
    val sending: Boolean = false,
    val error: String? = null,
    val tone: String = "Default",
    val textSize: TextUnit = 16.sp
)

///hilt injected viewModel for managing chat screen state
@HiltViewModel
class ChatViewModel @Inject constructor(
    ///injected repo
    private val repository: AppRepository
) : ViewModel() {

    ///internal mutable state
    private val _uiState = MutableStateFlow(UiState())
    ///immutable state for UI to observe
    val uiState: StateFlow<UiState> = _uiState

    ///keeps conversation history for AI
    private val conversation = mutableListOf<Message>(
        Message("system", "You are a helpful assistant.")
    )

    ///state update functions
    fun setInput(text: String) {
        _uiState.value = _uiState.value.copy(input = text)
    }

    //
    fun setTone(tone: String) {
        _uiState.value = _uiState.value.copy(tone = tone)
    }

    ///set text size for accessibility
    fun setTextSize(size: Float) {
        _uiState.value = _uiState.value.copy(textSize = size.sp)
    }


    fun send() {
        val text = _uiState.value.input
        if (text.isBlank()) return
        sendMessageInternal(text, isQuickAction = false)
    }

    ///quick action buttons
    fun sendQuickAction(action: String, contextText: String) {
        val prompt = when (action) {
            "hint" -> "Give me a hint about this: $contextText"
            "explain" -> "Please explain this again: $contextText"
            "translate" -> "Translate this into Spanish: $contextText"
            else -> contextText
        }
        sendMessageInternal(prompt, isQuickAction = true)
    }

    ///internal send handler
    private fun sendMessageInternal(text: String, isQuickAction: Boolean) {
        //only add user message for manual input
        if (!isQuickAction) {
            val userUiMessage = UiMessage(text, isUser = true)
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + userUiMessage,
                input = "",
                sending = true
            )
            conversation.add(Message("user", text))
        } else {
            _uiState.value = _uiState.value.copy(sending = true)
            conversation.add(Message("user", text))
        }

        viewModelScope.launch {
            val result = repository.sendMessage(conversation, _uiState.value.tone)
            val aiText = result.getOrElse { "Error: ${it.message}" }

            conversation.add(Message("assistant", aiText))

            val aiUiMessage = UiMessage(aiText, isUser = false)
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + aiUiMessage,
                sending = false,
                error = null
            )
        }
    }
}

