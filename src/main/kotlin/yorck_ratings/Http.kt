package yorck_ratings

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import java.util.concurrent.CompletableFuture

object Http {
    fun getAsync(url: String): CompletableFuture<String> {
        val result = CompletableFuture<String>()
        url.httpGet().responseString(object : Handler<String> {
            override fun success(request: Request, response: Response, value: String) {
                result.complete(value)
            }

            override fun failure(request: Request, response: Response, error: FuelError) {
                result.completeExceptionally(error)
            }
        })
        return result
    }
}
