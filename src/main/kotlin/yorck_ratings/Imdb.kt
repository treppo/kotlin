package yorck_ratings

import org.jsoup.Jsoup
import java.util.concurrent.CompletableFuture

data class ImdbSearchInfo(val title: String)

interface Imdb {
    fun getSearchInfo(title: String): CompletableFuture<ImdbSearchInfo?>
}

class AsyncImdb(private val imdbSearchUrl: String) : Imdb {
    override fun getSearchInfo(title: String): CompletableFuture<ImdbSearchInfo?> =
            Http.getAsync(imdbSearchUrl + title)
                    .thenApply { ImdbSearchParser.parse(it) }

}

object ImdbSearchParser {
    fun parse(html: String): ImdbSearchInfo? =
            Jsoup
                    .parse(html)
                    .select(".poster .title a")
                    .map { ImdbSearchInfo(it.text()) }
                    .firstOrNull()
}
