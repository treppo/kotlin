package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.util.concurrent.CompletableFuture

class YorckRatingsTest {
    @Test
    fun returnsYorckRatings() {
        val yorckTitle1 = "yt1"
        val yorckTitle2 = "yt2"
        val ratings = listOf(YorckRating(yorckTitle1, "it1"), (YorckRating(yorckTitle2, "it2")))
        val yorckWebsite = object : Yorck {
            override fun getInfos(): CompletableFuture<List<YorckInfo>> =
                    CompletableFuture.completedFuture(listOf(YorckInfo(yorckTitle1), YorckInfo(yorckTitle2)))
        }
        val imdb = object : Imdb {
            override fun getSearchInfo(title: String): CompletableFuture<ImdbSearchInfo?> =
                    if (title == yorckTitle1) CompletableFuture.completedFuture(ImdbSearchInfo("it1"))
                    else CompletableFuture.completedFuture(ImdbSearchInfo("it2"))
        }
        val yorckRatingsService = YorckRatings(yorckWebsite, imdb)

        val yorckRatings = yorckRatingsService.fetch().join()

        assert.that(yorckRatings, equalTo(ratings))
    }
}
