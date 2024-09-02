package example.com.plugins.data.yandexGptApi

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChatGtpResponseModel(
    val result: Result
)

@Serializable
data class Result(
    val alternatives: List<Alternative>,
    val usage: Usage,
    val modelVersion: String
)

@Serializable
data class Alternative(
    @SerializedName("message") val message: RequestMessage,
    val status: String
)

@Serializable
data class ResponseMessage(
    val role: String,
    val text: String
)

@Serializable
data class Usage(
    val inputTextTokens: String,
    val completionTokens: String,
    val totalTokens: String
)
