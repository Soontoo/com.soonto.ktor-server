package example.com.plugins.data.database

import example.com.AppConfig
import example.com.plugins.domain.YandexInteractor
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

suspend fun YandexInteractor.withDB(block: suspend Database.() -> Unit) = connectToPostgres(embedded = false).apply {
    transaction(this) {
        SchemaUtils.create(Users, Answers, Embeddings)
    }
    block()
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
fun connectToPostgres(embedded: Boolean): Database {

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