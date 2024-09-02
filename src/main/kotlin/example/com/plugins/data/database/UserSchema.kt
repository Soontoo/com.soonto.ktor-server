package example.com.plugins.data.database


import example.com.AppConfig
import example.com.plugins.utility.AppLogger
import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

@Serializable
data class User(
    val firstName: String,
    val lastName: String,
    val telegramId: Int,
    val isBot: Boolean
)


object Users : IntIdTable() {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val telegramId = integer("telegram_id").uniqueIndex() // Уникальный индекс для telegramId
    val isBot = bool("is_bot")
}

inline fun Database.withUserSchema(
    block: UsersSchema.() -> Unit,
    onError: (e: Exception) -> Unit = {}
) {
    try {
        UsersSchema(this).block()
    } catch (e: Exception) {
        AppLogger.log(e)
        onError(e)
    }
}
typealias DbUserId = Int

class UsersSchema(private val db: Database) {
    /**
     * Add user into database or update if exists and return user id
     */
    fun insertUser(user: User): DbUserId {
        return transaction(db) {
            val existingUser = Users.select { Users.telegramId eq user.telegramId }.singleOrNull()

            if (existingUser != null) {
                // Если пользователь существует, обновляем его данные
                Users.update({ Users.telegramId eq user.telegramId }) {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[isBot] = user.isBot
                }
                // Возвращаем id существующей записи
                existingUser[Users.id].value
            } else {
                // Если пользователь не существует, вставляем новую запись
                val newId = Users
                    .insertAndGetId {
                        it[firstName] = user.firstName
                        it[lastName] = user.lastName
                        it[telegramId] = user.telegramId
                        it[isBot] = user.isBot
                    }
                // Возвращаем id новой записи
                newId.value
            }
        }
    }
}

