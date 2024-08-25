package example.com.plugins.network

import example.com.plugins.yandexGptApi.RequestModel
import example.com.plugins.yandexGptApi.ResponseModel
import retrofit2.http.Body
import retrofit2.http.POST


interface YandexGptService {

    @POST("/foundationModels/v1/completion")
    suspend fun sendMessage(
        @Body requestModel: RequestModel
    ): ResponseModel
}

