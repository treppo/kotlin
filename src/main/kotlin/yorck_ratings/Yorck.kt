package yorck_ratings

import java.util.concurrent.CompletableFuture

interface Yorck {
    fun getInfos(): CompletableFuture<List<Result>>
}

class AsyncYorck(private val yorckUrl: String) : Yorck {
    override fun getInfos(): CompletableFuture<List<Result>> =
            Http.getAsync(yorckUrl)
                    .thenApply { YorckParser.parse(it) }
}
