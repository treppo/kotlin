package yorck_ratings

import org.jsoup.Jsoup
import java.util.concurrent.CompletableFuture

interface Imdb {
    fun getSearchInfo(term: String): CompletableFuture<Result>
}

class AsyncImdb(
        private val imdbSearchUrl: String,
        private val imdbUrl: String,
        private val get: (String) -> CompletableFuture<String>) : Imdb {

    override fun getSearchInfo(term: String): CompletableFuture<Result> =
            get(imdbSearchUrl + term)
                    .thenApply { parseSearchInfo(it) }

    private fun parseSearchInfo(html: String): Result =
            Jsoup
                    .parse(html)
                    .select(".poster .title a")
                    .map { Result(imdbTitle = it.text(), imdbUrl = imdbUrl + it.attr("href")) }
                    .firstOrNull() ?: Result()
}
