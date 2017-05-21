package yorck_ratings

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType.Text.Html
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.routing

class Application(configuration: Configuration) {
    val module: Application.() -> Unit = {
        install(Compression)

        routing {
            get("/") {
                val yorckRatingsService = YorckRatingsService(AsyncYorckWebsite(configuration.yorckUrl))
                val deferred = async(CommonPool) { yorckRatingsService.getYorckRatings() }
                call.respondText(deferred.await().joinToString("\n"), Html)
            }
        }
    }
}

fun main(args: Array<String>) {
    val application = Application(productionConfiguration())
    embeddedServer(Netty, 8080, module = application::module.get()).start(wait = true)
}
