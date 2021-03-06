package yorck_ratings

import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

data class Result(val yorckTitle: String = "_", val imdbTitle: String = "_", val imdbUrl: String = "#")

class YorckRatings(private val yorck: Yorck, val imdb: Imdb) {
    fun fetch(): List<Result> =
            yorck.getInfos()
                    .thenCompose { toRatings(it) }
                    .join()

    private fun toRatings(yorckInfos: List<Result>): CompletableFuture<List<Result>> {
        return allAsList(yorckInfos.map { rating ->
            imdb.getSearchInfo(rating.yorckTitle).thenApply { searchInfo ->
                rating.copy(imdbTitle = searchInfo.imdbTitle, imdbUrl = searchInfo.imdbUrl)
            }
        })
    }
}

fun <T> allAsList(futures: List<CompletableFuture<T>>): CompletableFuture<List<T>> {
    return CompletableFuture.allOf(*futures.toTypedArray())
            .thenApply { _ ->
                futures.stream()
                        .map { it.join() }
                        .collect(Collectors.toList())
            }
}
