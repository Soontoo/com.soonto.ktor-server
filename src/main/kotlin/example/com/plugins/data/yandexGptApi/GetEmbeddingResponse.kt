package example.com.plugins.data.yandexGptApi

import example.com.plugins.data.database.Embedding
import example.com.plugins.rest.models.QuestionModel
import example.com.plugins.utility.math.countScalar
import kotlinx.serialization.Serializable

@Serializable
data class GetEmbeddingResponse(
    val embedding: List<Double>,
    val numTokens: String,
    val modelVersion: String
)

fun Embedding.Companion.fromGetEmbeddingResponse(response: GetEmbeddingResponse, model: QuestionModel) = Embedding(
    embeddingVector = response.embedding,
    text = model.question,
    userId = model.telegramId,
    scalar = response.embedding.toDoubleArray().countScalar()
)