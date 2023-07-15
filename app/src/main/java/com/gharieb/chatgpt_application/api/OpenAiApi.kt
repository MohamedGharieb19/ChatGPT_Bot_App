package com.gharieb.chatgpt_application.api

import com.gharieb.chatgpt_application.data.CompletionRequest
import com.gharieb.chatgpt_application.data.CompletionResponse
import com.gharieb.chatgpt_application.module.Key.OPENAI_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {

    @Headers("Authorization: Bearer $OPENAI_API_KEY")
    @POST("v1/completions")
    suspend fun getCompletion(
        @Body completionRequest: CompletionRequest
    ): Response<CompletionResponse>

}