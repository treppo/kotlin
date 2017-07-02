package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class ImdbParserTest {
    @Test
    fun extractNothingFromEmptyHtml() {
        val emptyHtml = ""

        assertThat(ImdbParser.parse(emptyHtml), equalTo(Result()))
    }

    @Test
    fun extractMovieTitleFromHtml() {
        val html = javaClass.getResource("/imdb-search-arrival-response.html").readText()

        assertThat(ImdbParser.parse(html), equalTo(Result(imdbTitle = "Arrival")))
    }
}
