package example.com.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.includeCORS() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
}