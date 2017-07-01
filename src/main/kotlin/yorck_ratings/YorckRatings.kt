package yorck_ratings

data class YorckRating(val yorckTitle: String, val imdbTitle: String)

class YorckRatings(private val yorck: Yorck, val imdb: Imdb) {
    suspend fun getYorckRatings(): List<YorckRating> = yorck.getInfos().flatMap { yorckInfo ->
        val searchInfo = imdb.getSearchInfo(yorckInfo.title)
        if (searchInfo != null) listOf(YorckRating(yorckInfo.title, searchInfo.title))
        else emptyList()
    }
}
