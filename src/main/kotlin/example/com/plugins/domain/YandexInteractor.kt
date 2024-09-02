package example.com.plugins.domain

import example.com.plugins.data.database.*
import example.com.plugins.data.repositories.Result
import example.com.plugins.data.repositories.YandexGptRepository
import example.com.plugins.data.repositories.mapModel
import example.com.plugins.data.yandexGptApi.fromGetEmbeddingResponse
import example.com.plugins.rest.models.QuestionModel
import example.com.plugins.rest.models.toAnswer
import example.com.plugins.rest.models.toUser
import example.com.plugins.utility.AppLogger
import example.com.plugins.utility.math.countCosineDistance
import example.com.plugins.utility.printlnRED
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.withYandexInteractor(block: YandexInteractor.() -> Unit) = block(YandexInteractor())

class YandexInteractor(private val repo: IYandexGptRepository = YandexGptRepository()) {

    suspend fun createEmbedding(question: QuestionModel): Result<Embedding> {
        val response =
            repo.getEmbedding(question.question).mapModel { Embedding.fromGetEmbeddingResponse(it, question) }
        if (response is Result.Success) {
            withDB {
                withUserSchema(
                    block = {
                        withEmbeddingSchema(
                            block = {
                                saveEmbedding(response.data, insertUser(question.toUser()))
                            }
                        )
                    }
                )
            }

        }
        return response
    }

    suspend fun sendQuestion(question: QuestionModel): Result<String> {
        val result = repo.sendQuestion(question.question)
        try {
            if (result is Result.Success) {
                withDB {
                    saveAnswer(result.data, question)
                }
            }
        } catch (e: Exception) {
            AppLogger.log(e)
        }
        return result
    }

    suspend fun findAnswer(question: QuestionModel): Result<Pair<Embedding, Double>> {
        return when (val questionEmbedding =
            repo.getEmbedding(question.question).mapModel { Embedding.fromGetEmbeddingResponse(it, question) }) {
            is Result.Success -> {
                var result = Pair(questionEmbedding.data, 0.0)
                withDB {
                    withEmbeddingSchema(
                        block = {
                            result = findMostSimilarEmbedding(questionEmbedding.data, getEmbeddingList())
                        }
                    )

                }
                return Result.Success(result)
            }

            is Result.Error -> Result.Error(questionEmbedding.error)
        }
    }
}

private fun YandexInteractor.findMostSimilarEmbedding(
    questionEmbedding: Embedding,
    embeddingList: List<Embedding>
): Pair<Embedding, Double> {
    var candidate = embeddingList.first()
    var minDistance = 1.0
    for (embedding in embeddingList) {
        val distance = countCosineDistance(
            embedding.embeddingVector.toDoubleArray(),
            questionEmbedding.embeddingVector.toDoubleArray(),
            embedding.scalar,
            questionEmbedding.scalar
        )
        printlnRED("Distance: $distance   " + embedding.text)
        if (minDistance > distance) {
            minDistance = distance
            candidate = embedding
        }
    }
    return Pair(candidate, minDistance)
}

private fun Database.saveAnswer(answer: String, model: QuestionModel) {
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


