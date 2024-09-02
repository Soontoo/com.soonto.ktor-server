package example.com.plugins.data.database

import example.com.plugins.utility.AppLogger
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

data class Embedding(
    val text: String,
    val embeddingVector: List<Double>,
    val scalar: Double,
    val userId: Int
) {
    companion object
}

object Embeddings : IntIdTable() {
    val text = text("text")
    val embeddingVector = text("embedding_vector")
    val scalar = double("scalar")
    val userId = integer("user_id").references(Users.id)
}

inline fun Database.withEmbeddingSchema(
    block: EmbeddingSchema.() -> Unit,
    onError: (e: Exception) -> Unit = {}
) {
    try {
        EmbeddingSchema(this).block()
    } catch (e: Exception) {
        AppLogger.log(e)
        onError(e)
    }
}

class EmbeddingSchema(private val db: Database) {
    fun saveEmbedding(embedding: Embedding, id: DbUserId) {
        return transaction(db) {
            // Вставка новой записи в таблицу Embeddings
            Embeddings.insert {
                it[text] = embedding.text
                it[embeddingVector] = embedding.embeddingVector.joinToString(",")
                it[scalar] = embedding.scalar
                it[userId] = id
            }
        }
    }

    fun getEmbeddingList(): List<Embedding> {
        return transaction(db) {
            Embeddings.selectAll().map {
                Embedding(
                    text = it[Embeddings.text],
                    embeddingVector = it[Embeddings.embeddingVector].split(",").map { it.toDouble() },
                    scalar = it[Embeddings.scalar],
                    userId = it[Embeddings.userId]
                )
            }
        }
    }
}