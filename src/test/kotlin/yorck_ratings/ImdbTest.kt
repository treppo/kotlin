package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class ImdbTest {
    val imdbUrl = "imdbSearchUrl"
    val imdbSearchUrl = "imdbSearchUrl"

    @Test
    fun extractNothingFromEmptyHtml() {
        val html = ""

        val actualResult = AsyncImdb(imdbSearchUrl, imdbUrl, HttpMock.getAsync(html)).getSearchInfo("term").join()

        assertThat(actualResult, equalTo(Result()))
    }

    @Test
    fun extractMovieTitleAndUrlFromHtml() {
        val html = javaClass.getResource("/imdb-search-arrival-response.html").readText()

        val actualResult = AsyncImdb(imdbSearchUrl, imdbUrl, HttpMock.getAsync(html)).getSearchInfo("Arrival").join()

        assertThat(actualResult, equalTo(Result(imdbTitle = "Arrival", imdbUrl = "$imdbUrl/title/tt2543164/?ref_=m_fn_al_1")))
    }
}
