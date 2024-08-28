package example.com.plugins.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.initTables() = withDB {
    transaction(this) {
        SchemaUtils.create(Users, Answers)
    }
}