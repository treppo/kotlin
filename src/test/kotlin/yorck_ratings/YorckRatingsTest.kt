package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.util.concurrent.CompletableFuture

class YorckRatingsTest {
    @Test
    fun returnsYorckRatings() {
        val yorckResult1 = Result(yorckTitle = "yt1")
        val yorckResult2 = Result(yorckTitle = "yt2")
        val result1 = Result(yorckTitle = "yt1", imdbTitle = "it1", imdbUrl = "iu1")
        val result2 = Result(yorckTitle = "yt2", imdbTitle = "it2", imdbUrl = "iu2")
        val results = listOf(result1, result2)
        val yorckWebsite = object : Yorck {
            override fun getInfos(): CompletableFuture<List<Result>> =
                    CompletableFuture.completedFuture(listOf(yorckResult1, yorckResult2))
        }
        val imdb = object : Imdb {
            override fun getSearchInfo(term: String): CompletableFuture<Result> =
                    if (term == result1.yorckTitle) CompletableFuture.completedFuture(result1)
                    else CompletableFuture.completedFuture(result2)
        }
        val yorckRatings = YorckRatings(yorckWebsite, imdb)

        val actualResults = yorckRatings.fetch()

        assert.that(actualResults, equalTo(results))
    }
}
