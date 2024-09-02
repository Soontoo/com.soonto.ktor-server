package example.com.plugins.data.yandexGptApi

import example.com.AppConfig
import kotlinx.serialization.Serializable

@Serializable
data class ChatGtpRequestModel(
    val modelUri: String = "gpt://${AppConfig.yandexFolderId}/yandexgpt-lite",
    val completionOptions: CompletionOptions = CompletionOptions(stream = false, temperature = 0.1, maxTokens = "1000"),
    val messages: List<RequestMessage>
)

@Serializable
data class CompletionOptions(
    val stream: Boolean,
    val temperature: Double,
    val maxTokens: String
)
@Serializable
data class RequestMessage(
    val role: String = "user",
    val text: String
)


