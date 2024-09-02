package example.com

import example.com.plugins.*
import example.com.plugins.rest.configureGptREST
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    AppConfig.initialize(this.environment)
    configureSockets()
    configureSerialization()
    configureGptREST()
    configureHTTP()
    configureSecurity()
    configureRouting()
    includeCORS()
    configureGptREST()
}
