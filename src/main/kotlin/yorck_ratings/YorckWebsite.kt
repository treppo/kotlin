package yorck_ratings

data class YorckInfo(val title: String)

interface YorckWebsite {
    suspend fun getInfos(): List<YorckInfo>
}

class AsyncYorckWebsite(private val yorckUrl: String): YorckWebsite {
    override suspend fun getInfos(): List<YorckInfo> = YorckInfoParser.parse(AsyncHttp.get(yorckUrl))
}
