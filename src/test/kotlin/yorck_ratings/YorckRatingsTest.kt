package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class YorckRatingsTest {
    @Test
    fun returnsYorckRatings() {
        val yorckTitle1 = "yt1"
        val yorckTitle2 = "yt2"
        val ratings = listOf(YorckRating(yorckTitle1, "it1"), YorckRating(yorckTitle2, "it2"))
        val yorckWebsite = object : Yorck {
            override suspend fun getInfos(): List<YorckInfo> =
                    listOf(YorckInfo(yorckTitle1), YorckInfo(yorckTitle2))
        }
        val imdb = object : Imdb {
            override suspend fun getSearchInfo(title: String): ImdbSearchInfo =
                    if (title == yorckTitle1) ImdbSearchInfo("it1")
                    else ImdbSearchInfo("it2")
        }
        val yorckRatingsService = YorckRatings(yorckWebsite, imdb)

        val deferredYorckRatings = async(CommonPool) { yorckRatingsService.getYorckRatings() }

        runBlocking {
            assert.that(deferredYorckRatings.await(), equalTo(ratings))
        }
    }
}
