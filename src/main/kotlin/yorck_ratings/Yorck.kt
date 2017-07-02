package yorck_ratings

import org.jsoup.Jsoup
import java.util.concurrent.CompletableFuture

interface Yorck {
    fun getInfos(): CompletableFuture<List<Result>>
}

class AsyncYorck(private val yorckUrl: String, private val get: (String) -> CompletableFuture<String>) : Yorck {
    override fun getInfos(): CompletableFuture<List<Result>> =
            get(yorckUrl)
                    .thenApply { parse(it) }

    private fun parse(it: String): List<Result> {
        return Jsoup
                .parse(it)
                .select(".movie-info .movie-details h2")
                .map { Result(yorckTitle = it.text()) }
    }
}
