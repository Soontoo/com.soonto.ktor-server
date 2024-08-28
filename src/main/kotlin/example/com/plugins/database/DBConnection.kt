package example.com.plugins.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.withDB(block: Database.() -> Unit) = connectToPostgres(embedded = false).block()
