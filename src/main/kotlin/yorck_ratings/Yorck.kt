package yorck_ratings

data class YorckInfo(val title: String)

interface Yorck {
    suspend fun getInfos(): List<YorckInfo>
}

class AsyncYorck(private val yorckUrl: String): Yorck {
    override suspend fun getInfos(): List<YorckInfo> = YorckInfoParser.parse(AsyncHttp.get(yorckUrl))
}
