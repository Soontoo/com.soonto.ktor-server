package example.com

import example.com.plugins.*
import example.com.plugins.rest.configureGptREST
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore

fun main() {
    AppConfig.initialize()
    embeddedServer(Netty, port = AppConfig.port.toInt()) {
        routing {
            get("/*") {
                call.respondRedirect(
                    "https://${call.request.host()}:${AppConfig.sslPort}${call.request.uri}",
                    permanent = true
                )
            }
        }
        routing {
            get("") {
                call.respondRedirect(
                    "https://${call.request.host()}:${AppConfig.sslPort}${call.request.uri}",
                    permanent = true
                )
            }
        }
    }.start(wait = false)

    embeddedServer(
        factory = Netty,
        environment = applicationEnvironment { log = LoggerFactory.getLogger("ktor.application") },
        configure = { envConfig() },
        module = Application::module
    ).start(wait = true)
}

private fun ApplicationEngine.Configuration.envConfig() {

    val keyStoreFile = File("keystore.jks")
    sslConnector(
        keyStore = KeyStore.getInstance(File("keystore.jks"), AppConfig.keyStorePassword.toCharArray()),
        keyAlias = "sampleAlias",
        keyStorePassword = { AppConfig.keyStorePassword.toCharArray() },
        privateKeyPassword = { AppConfig.keyStorePassword.toCharArray() }) {
        port = 8443
        keyStorePath = keyStoreFile
    }
}


fun Application.module() {
    configureSockets()
    configureSerialization()
    configureGptREST()
    configureHTTP()
    configureRouting()
    includeCORS()
    configureGptREST()
}
