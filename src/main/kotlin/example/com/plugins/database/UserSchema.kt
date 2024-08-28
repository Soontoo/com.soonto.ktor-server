package example.com.plugins.database


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

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Database {

    Class.forName("org.postgresql.Driver")
    if (embedded) {
        return Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = AppConfig.databaseUrl
        val user = AppConfig.databaseUser
        val password = AppConfig.databasePassword
        return Database.connect(url = url, user = user, password = password)
    }
}