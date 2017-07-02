package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.util.concurrent.CompletableFuture

class YorckRatingsTest {
    @Test
    fun returnsYorckRatings() {
        val result1 = Result(yorckTitle = "yt1", imdbTitle = "it1")
        val result2 = Result(yorckTitle = "yt2", imdbTitle = "it2")
        val results = listOf(result1, result2)
        val yorckWebsite = object : Yorck {
            override fun getInfos(): CompletableFuture<List<Result>> =
                    CompletableFuture.completedFuture(listOf(result1, result2))
        }
        val imdb = object : Imdb {
            override fun getSearchInfo(title: String): CompletableFuture<Result> =
                    if (title == result1.yorckTitle) CompletableFuture.completedFuture(result1)
                    else CompletableFuture.completedFuture(result2)
        }
        val yorckRatings = YorckRatings(yorckWebsite, imdb)

        val actualResults = yorckRatings.fetch().join()

        assert.that(actualResults, equalTo(results))
    }
}
