package example.com.plugins.rest

import example.com.plugins.database.*
import example.com.plugins.repositories.Result
import example.com.plugins.rest.models.QuestionModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureGptREST() {
    withDB {
        routing {
            post("/askGpt") {
                val model = call.receive<QuestionModel>()
                //when (val answer = yandexGptApi.sendQuestion(model.question)) {
                when (val answer = Result.Success("testAnswer")) {
                    is Result.Success -> {
                        saveAnswer(answer.data, model)
                        call.respond(HttpStatusCode.OK, answer.data)
                    }
                    //is Result.Error -> call.respond(HttpStatusCode.InternalServerError, answer.error)
                }
            }
        }
    }
}

private suspend inline fun Database.saveAnswer(answer: String, model: QuestionModel) {
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

