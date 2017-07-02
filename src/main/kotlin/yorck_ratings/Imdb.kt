package yorck_ratings

import org.jsoup.Jsoup
import java.util.concurrent.CompletableFuture

interface Imdb {
    fun getSearchInfo(title: String): CompletableFuture<Result>
}

class AsyncImdb(private val imdbSearchUrl: String) : Imdb {
    override fun getSearchInfo(title: String): CompletableFuture<Result> =
            Http.getAsync(imdbSearchUrl + title)
                    .thenApply { ImdbParser.parse(it) }

}

object ImdbParser {
    fun parse(html: String): Result =
            Jsoup
                    .parse(html)
                    .select(".poster .title a")
                    .map { Result(imdbTitle = it.text()) }
                    .firstOrNull() ?: Result()
}
