package example.com.plugins.rest

import example.com.plugins.data.repositories.Result
import example.com.plugins.domain.withYandexInteractor
import example.com.plugins.rest.models.QuestionModel
import example.com.plugins.rest.models.WebCardModel
import example.com.plugins.rest.models.WebcardResponse
import example.com.plugins.utility.AppLogger
import example.com.plugins.wsSessionsConnections
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import java.io.File


@Serializable
data class AnswerResponse(val answer: String)

fun Application.configureGptREST() {

    withYandexInteractor {
        routing {
            post("/askGpt") {
                val model = call.receive<QuestionModel>()
                AppLogger.log("POST: /askGpt body: ${model.question}")
                when (val answer = sendQuestion(model)) {
                    //when (val answer = Result.Success("testAnswer")) {
                    is Result.Success -> {
                        call.respond(HttpStatusCode.OK, AnswerResponse(answer.data))
                        AppLogger.log("POST: /askGpt body response: ${answer.data}")
                    }
                    is Result.Error -> {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            answer.error.message ?: "Unknown error"
                        )
                        AppLogger.log("POST: /askGpt body response: ${answer.error.message}")
                        AppLogger.log(answer.error)
                    }
                }
            }

            post("/saveAnswer") {
                val model = call.receive<QuestionModel>()
                AppLogger.log("POST: /saveAnswer body: ${model.question}")
                when (val result = createEmbedding(model)) {
                    is Result.Success -> {
                        call.respond(HttpStatusCode.OK)
                        AppLogger.log("POST: /saveAnswer body response: ${result.data}")
                    }
                    is Result.Error -> {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            result.error.message ?: "Unknown error"
                        )
                        AppLogger.log("POST: /saveAnswer body response: ${result.error}")
                        AppLogger.log(result.error)
                    }
                }
            }

            get("/findAnswer") {
                val model = call.receive<QuestionModel>()
                AppLogger.log("GET: /findAnswer body: ${model.question}")
                when (val answer = findAnswer(model)) {
                    is Result.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            AnswerResponse(answer.data.first.text + " " + answer.data.second.toString())
                        )
                        AppLogger.log("GET: /findAnswer body response: ${answer.data}")
                    }

                    is Result.Error -> {
                        AppLogger.log("GET: /findAnswer body response: ${answer.error}")
                        AppLogger.log(answer.error)
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            answer.error.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    routing {
        post("/writeToTftScreen") {
            val model = call.receive<String>()
            wsSessionsConnections.forEach {
                it.outgoing.send(Frame.Text(model))
            }
            call.respond(HttpStatusCode.OK)
        }
        get("/getPhoto") {
            wsSessionsConnections.forEach {
                it.outgoing.send(Frame.Text("test_send_image"))
            }
            call.response.header(HttpHeaders.ContentType, "image/jpeg")
            call.respond(photoRawFlow.first())
        }

        post("/sendPhoto") {
            AppLogger.log("call /sendPhoto")
            val photo = call.receive<ByteArray>()
            AppLogger.log(photo.size.toString())
            photoRawFlow.emit(photo)
            File("src/main/resources/images/sendingPhoto.jpg").writeBytes(photo)
            call.respond(HttpStatusCode.OK)
        }
        get("/getWebCardList") {
            AppLogger.log("call /getWebCardList")
            call.respond(HttpStatusCode.OK, WebcardResponse(listWebCard))
            AppLogger.log(WebcardResponse(listWebCard).toString())
        }
    }
}

val listWebCard = mutableListOf(WebCardModel("test", "test", "test")).apply {
    repeat(10) { add(WebCardModel("test$it", "test$it", "test$it")) }
}
val photoRawFlow = MutableSharedFlow<ByteArray>()
