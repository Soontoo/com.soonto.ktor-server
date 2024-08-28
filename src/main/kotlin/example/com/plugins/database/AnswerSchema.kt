package example.com.plugins.database

import example.com.plugins.utility.AppLogger
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

data class Answer(
    val answer: String,
    val question: String,
    val userId: Int,
)

object Answers : IntIdTable() {
    val answer = varchar("answer", 255)
    val question = varchar("question", 255)
    val userId = integer("user_id").references(Users.id) // Внешний ключ, ссылающийся на таблицу Users
    val data = long("data") // Поле для хранения даты и времени
}

inline fun Database.withAnswerSchema(
    block: AnswerSchema.() -> Unit,
    onError: (e: Exception) -> Unit = {}
) {
    try {
        AnswerSchema(this).block()
    } catch (e: Exception) {
        AppLogger.log(e)
        onError(e)
    }
}

class AnswerSchema(private val db: Database) {
    fun insertAnswer(answerModel: Answer) {
        return transaction(db) {
            // Вставка новой записи в таблицу Answers
            val newId = Answers
                .insertAndGetId {
                    it[answer] = answerModel.answer
                    it[question] = answerModel.question
                    it[userId] = answerModel.userId
                    it[data] = System.currentTimeMillis()
                }
            // Возвращаем id новой записи
            newId.value
        }
    }
}