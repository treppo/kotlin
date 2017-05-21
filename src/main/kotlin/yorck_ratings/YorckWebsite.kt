package yorck_ratings

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import kotlin.coroutines.experimental.suspendCoroutine

data class YorckInfo(val title: String)

interface YorckWebsite {
    suspend fun getInfos(): List<YorckInfo>
}

class AsyncYorckWebsite(private val yorckUrl: String): YorckWebsite {
    override suspend fun getInfos(): List<YorckInfo> =
        listOf(YorckInfo(AsyncHttp.get(yorckUrl)))
}

object AsyncHttp {
    suspend fun get(url: String): String =
            suspendCoroutine { cont ->
                url.httpGet().responseString(object : Handler<String> {
                    override fun success(request: Request, response: Response, value: String) {
                        cont.resume(value)
                    }

                    override fun failure(request: Request, response: Response, error: FuelError) {
                        cont.resumeWithException(error)
                    }
                })
            }
}
