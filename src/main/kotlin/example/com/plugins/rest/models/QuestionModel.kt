package example.com.plugins.rest.models

import kotlinx.serialization.Serializable

@Serializable
data class QuestionModel(
    val firstName: String,
    val lastName: String,
    val telegramId: Int,
    val isBot: Boolean,
    val question: String
)