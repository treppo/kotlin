package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class YorckRatingsServiceTest {
    @Test
    fun returnsYorckRatings() {
        val infos = listOf(YorckInfo("title1"), YorckInfo("title2"))
        val ratings = listOf(YorckRating("title1"), YorckRating("title2"))
        val yorckWebsite = object : YorckWebsite {
            override suspend fun getInfos(): List<YorckInfo> = infos
        }
        val yorckRatingsService = YorckRatingsService(yorckWebsite)

        val deferredYorckRatings = async(CommonPool) { yorckRatingsService.getYorckRatings() }

        runBlocking {
            assert.that(deferredYorckRatings.await(), equalTo(ratings))
        }
    }
}
