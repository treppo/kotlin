package yorck_ratings

import com.github.kittinunf.fuel.httpGet
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType.Text.Html
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.routing

class Application(configuration: Configuration) {
    val module: Application.() -> Unit = {
        install(DefaultHeaders)
        install(Compression)

        routing {
            get("/") {
                val (_, _, result) = configuration.yorckUrl.httpGet().responseString()
                call.respondText(result.get(), Html)
            }
        }
    }
}

fun main(args: Array<String>) {
    val application = Application(Configuration(""))
    embeddedServer(Netty, 8080, module = application::module.get()).start(wait = true)
}
