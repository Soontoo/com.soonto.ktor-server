package example.com.plugins.data.repositories

import example.com.plugins.data.yandexGptApi.ChatGtpRequestModel
import example.com.plugins.data.yandexGptApi.RequestMessage
import example.com.plugins.data.yandexGptApi.yandexGptApi
import example.com.plugins.domain.IYandexGptRepository
import example.com.plugins.data.yandexGptApi.GetEmbeddingRequest
import example.com.plugins.data.yandexGptApi.GetEmbeddingResponse
import example.com.plugins.network.YandexGptService


class YandexGptRepository(private val api: YandexGptService = yandexGptApi) : IYandexGptRepository {

    override suspend fun sendQuestion(question: String): Result<String> {
        return handleRequestError {

            api.sendMessage(ChatGtpRequestModel(messages = listOf(RequestMessage(text = question)))).result.alternatives.joinToString {
                it.message.text
            }
        }
    }

    override suspend fun getEmbedding(question: String): Result<GetEmbeddingResponse> {
        return handleRequestError {
            api.getEmbedding(GetEmbeddingRequest(text = question))
        }
    }
}
