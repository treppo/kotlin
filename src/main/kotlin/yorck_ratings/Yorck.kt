package yorck_ratings

import java.util.concurrent.CompletableFuture

data class YorckInfo(val title: String)

interface Yorck {
    fun getInfos(): CompletableFuture<List<YorckInfo>>
}

class AsyncYorck(private val yorckUrl: String) : Yorck {
    override fun getInfos(): CompletableFuture<List<YorckInfo>> = Http.getAsync(yorckUrl).thenApply { YorckInfoParser.parse(it) }
}
