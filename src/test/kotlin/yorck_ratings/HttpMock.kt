package yorck_ratings

import java.util.concurrent.CompletableFuture

object HttpMock {
    fun getAsync(html: String): (String) -> CompletableFuture<String> =
            { _ -> CompletableFuture.completedFuture(html) }
}
