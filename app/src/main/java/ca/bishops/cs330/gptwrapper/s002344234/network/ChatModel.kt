package ca.bishops.cs330.gptwrapper.s002344234.network

///represents body of chat request sent to OpenAI API
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>
)

///single message in API request
data class Message(
    val role: String,
    val content: String
)

///response body
data class ChatResponse(
    val choices: List<Choice>
)
///single choice in response
data class Choice(
    val message: Message
)
