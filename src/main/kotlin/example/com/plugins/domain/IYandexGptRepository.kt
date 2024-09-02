package example.com.plugins.domain

import example.com.plugins.data.repositories.Result
import example.com.plugins.data.yandexGptApi.GetEmbeddingResponse

interface IYandexGptRepository {
    suspend fun sendQuestion(question: String): Result<String>
    suspend fun getEmbedding(question: String): Result<GetEmbeddingResponse>
}
