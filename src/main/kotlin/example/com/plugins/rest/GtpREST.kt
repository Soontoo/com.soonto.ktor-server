package example.com.plugins.rest

import example.com.plugins.data.repositories.Result
import example.com.plugins.domain.withYandexInteractor
import example.com.plugins.rest.models.QuestionModel
import example.com.plugins.wsSessionsConnections
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable


@Serializable
data class AnswerResponse(val answer: String)

fun Application.configureGptREST() {

    withYandexInteractor {
        routing {
            post("/askGpt") {

                val model = call.receive<QuestionModel>()
                when (val answer = sendQuestion(model)) {
                    //when (val answer = Result.Success("testAnswer")) {
                    is Result.Success -> call.respond(HttpStatusCode.OK, AnswerResponse(answer.data))
                    is Result.Error -> call.respond(
                        HttpStatusCode.InternalServerError,
                        answer.error.message ?: "Unknown error"
                    )
                }
            }

            post("/saveAnswer") {
                val model = call.receive<QuestionModel>()
                when (val result = createEmbedding(model)) {
                    is Result.Success -> call.respond(HttpStatusCode.OK)
                    is Result.Error -> call.respond(
                        HttpStatusCode.InternalServerError,
                        result.error.message ?: "Unknown error"
                    )
                }
            }

            get("/findAnswer") {
                val model = call.receive<QuestionModel>()
                when (val answer = findAnswer(model)) {
                    is Result.Success -> call.respond(
                        HttpStatusCode.OK,
                        AnswerResponse(answer.data.first.text + " " + answer.data.second.toString())
                    )

                    is Result.Error -> call.respond(
                        HttpStatusCode.InternalServerError,
                        answer.error.message ?: "Unknown error"
                    )
                }
            }
            post("/writeToTftScreen") {
                val model = call.receive<String>()
                wsSessionsConnections.forEach {
                    it.outgoing.send(Frame.Text(model))
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

