package yorck_ratings

data class YorckRating(val yorckTitle: String)

class YorckRatingsService(private val yorckWebsite: YorckWebsite) {
    suspend fun getYorckRatings(): List<YorckRating> = yorckWebsite.getInfos().map { yorckInfo ->
        YorckRating(yorckInfo.title)
    }
}
