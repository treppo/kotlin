package yorck_ratings

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
                val yorckRatings =
                        YorckRatings(
                                AsyncYorck(configuration.yorckUrl, Http::getAsync),
                                AsyncImdb(configuration.imdbSearchUrl, configuration.imdbUrl, Http::getAsync))
                call.respondText(view(yorckRatings.fetch()), Html)
            }
        }
    }
}

fun main(args: Array<String>) {
    val application = Application(productionConfiguration())
    embeddedServer(Netty, 8080, module = application::module.get()).start(wait = true)
}

private fun view(results: List<Result>): String =
        """
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Yorck Movies with IMDB ratings</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <ol>
            ${results.map { it.asListItem() }.joinToString("\n")}
        </ol>
    </body>
</html>
"""

fun Result.asListItem(): String = """<li><a href="${this.imdbUrl}">${this.imdbTitle}</a> • ${this.yorckTitle}</li>"""
