package example.com.plugins.rest

import example.com.plugins.database.*
import example.com.plugins.repositories.Result
import example.com.plugins.repositories.sendQuestion
import example.com.plugins.rest.models.QuestionModel
import example.com.plugins.yandexGptApi.yandexGptApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database


@Serializable
data class AnswerResponse(val answer: String)

fun Application.configureGptREST() {
    withDB {
        routing {
            post("/askGpt") {
                val model = call.receive<QuestionModel>()
                when (val answer = yandexGptApi.sendQuestion(model.question)) {
                    //when (val answer = Result.Success("testAnswer")) {
                    is Result.Success -> {
                        saveAnswer(answer.data, model)
                        call.respond(HttpStatusCode.OK, AnswerResponse(answer.data))
                    }

                    is Result.Error -> call.respond(HttpStatusCode.InternalServerError, answer.error)
                }
            }
        }
    }
}

private inline fun Database.saveAnswer(answer: String, model: QuestionModel) {
    withUserSchema(
        block = {
            withAnswerSchema(
                block = {
                    insertAnswer(model.toAnswer(answer, insertUser(model.toUser())))
                }
            )
        },
    )
}

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

