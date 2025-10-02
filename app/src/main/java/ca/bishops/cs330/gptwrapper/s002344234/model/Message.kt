package ca.bishops.cs330.gptwrapper.s002344234.model


///single chat message in UI
data class Message(
    ///content
    val text: String,

    ///true if message was sent by user, false if from AI
    val isUser: Boolean
)
