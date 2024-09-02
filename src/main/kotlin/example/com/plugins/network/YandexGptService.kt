package example.com.plugins.network

import example.com.plugins.data.yandexGptApi.ChatGtpRequestModel
import example.com.plugins.data.yandexGptApi.ChatGtpResponseModel
import example.com.plugins.data.yandexGptApi.GetEmbeddingRequest
import example.com.plugins.data.yandexGptApi.GetEmbeddingResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface YandexGptService {

    @POST("/foundationModels/v1/completion")
    suspend fun sendMessage(
        @Body requestModel: ChatGtpRequestModel
    ): ChatGtpResponseModel

    @POST("/foundationModels/v1/textEmbedding")
    suspend fun getEmbedding(
        @Body requestModel: GetEmbeddingRequest
    ): GetEmbeddingResponse
}

