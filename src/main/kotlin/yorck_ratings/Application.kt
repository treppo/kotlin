package yorck_ratings

import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType.Text.Html
import org.jetbrains.ktor.jetty.Jetty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.routing

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello, world!", Html)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Jetty, 8080, module = Application::module).start(wait = true)
}
