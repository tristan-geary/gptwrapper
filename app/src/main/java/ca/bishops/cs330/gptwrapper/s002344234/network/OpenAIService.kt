package ca.bishops.cs330.gptwrapper.s002344234.network

import retrofit2.http.Body
import retrofit2.http.POST

///defining OpenAi API endpoints
interface OpenAIService {

    ///sends chat request to openAi API
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse
}
