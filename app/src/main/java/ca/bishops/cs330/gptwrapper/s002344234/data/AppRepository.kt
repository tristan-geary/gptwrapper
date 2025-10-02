package ca.bishops.cs330.gptwrapper.s002344234.data

import ca.bishops.cs330.gptwrapper.s002344234.network.ChatRequest
import ca.bishops.cs330.gptwrapper.s002344234.network.Message
import ca.bishops.cs330.gptwrapper.s002344234.network.OpenAIService
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val service: OpenAIService
) {
    suspend fun sendMessage(conversation: List<Message>, tone: String): Result<String> {
        return try {
            ///add tone instruction at the beginning
            val modifiedConversation = buildList {
                add(
                    Message(
                        "system",
                        if (tone == "Default") {
                            "You are a helpful assistant."
                        } else {
                            "You are a helpful assistant. Please respond in a $tone tone."
                        }
                    )
                )
                ///add rest of conversation
                addAll(conversation.filter { it.role != "system" })
            }


            ///create request object for OpenAi API
            val request = ChatRequest(messages = modifiedConversation)

            ///send requestion via OpenAI API
            val response = service.sendMessage(request)


            ///extract AI's reply from response
            val reply = response.choices.firstOrNull()?.message?.content
                ?: "(no response from model)"
            Result.success(reply)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


