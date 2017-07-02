package yorck_ratings

import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

data class YorckRating(val yorckTitle: String, val imdbTitle: String)

class YorckRatings(private val yorck: Yorck, val imdb: Imdb) {
    fun fetch(): CompletableFuture<List<YorckRating>> =
            yorck.getInfos()
                    .thenCompose { toRatings(it) }
                    .thenApply { it.filterNotNull() }

    private fun toRatings(yorckInfos: List<YorckInfo>): CompletableFuture<List<YorckRating?>> {
        return allAsList(yorckInfos.map { (title) ->
            imdb.getSearchInfo(title).thenApply { searchInfo ->
                if (searchInfo != null) YorckRating(title, searchInfo.title)
                else null
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
