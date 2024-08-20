package example.com

import example.com.plugins.*
import example.com.plugins.database.configureDatabases
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
