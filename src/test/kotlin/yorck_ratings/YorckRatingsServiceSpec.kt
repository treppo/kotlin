package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object YorckRatingsServiceSpec : Spek({
    describe("YorckRatingsService") {
        it("returns the yorck ratings") {
            val yorckWebsite = object : YorckWebsite {
                override suspend fun getInfos() = listOf(YorckInfo("title1"))
            }
            val yorckRatingsService = YorckRatingsService(yorckWebsite)

            val deferredYorckRatings = async(CommonPool) { yorckRatingsService.getYorckRatings() }

            runBlocking {
                assert.that(deferredYorckRatings.await(), equalTo(listOf(YorckRating("title1"))))
            }
        }
    }
})
