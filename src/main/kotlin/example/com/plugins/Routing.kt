package example.com.plugins

import example.com.AppConfig
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import java.io.File

fun Application.configureRouting() {
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }

    install(SwaggerUI) {

        info {
            title = "Example API"
            version = "latest"
            description = "Example API for testing and demonstration purposes."
        }
        server {
            url = AppConfig.serverUrl
            description = "Remote server"
        }
    }
    routing {
        singlePageApplication{
//            react("src/main/kotlin/example/com/web")
            useResources = true
            filesPath = "web"
            defaultPage = "index.html"
        }
        swaggerUI(path = "/swagger-ui", swaggerFile = "openapi/documentation.yaml")
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
    }
}
