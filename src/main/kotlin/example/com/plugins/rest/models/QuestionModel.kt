package example.com.plugins.rest.models

import example.com.plugins.data.database.Answer
import example.com.plugins.data.database.User
import kotlinx.serialization.Serializable

@Serializable
data class QuestionModel(
    val firstName: String,
    val lastName: String,
    val telegramId: Int,
    val isBot: Boolean,
    val question: String
)

fun QuestionModel.toAnswer(answer: String, userId: Int): Answer {
    return Answer(
        answer = answer,
        question = this.question,
        userId = userId,
    )
}

fun QuestionModel.toUser(): User {
    return User(
        firstName = this.firstName,
        lastName = this.lastName,
        telegramId = this.telegramId,
        isBot = this.isBot,
    )
}