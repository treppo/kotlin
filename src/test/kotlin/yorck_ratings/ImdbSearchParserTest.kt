package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class ImdbSearchParserTest {
    @Test
    fun extractNothingFromEmptyHtml() {
        val emptyHtml = ""

        assertThat(ImdbSearchParser.parse(emptyHtml), equalTo<ImdbSearchInfo>(null))
    }

    @Test
    fun extractMovieTitleFromHtml() {
        val html = javaClass.getResource("/imdb-search-arrival-response.html").readText()

        assertThat(ImdbSearchParser.parse(html), equalTo(ImdbSearchInfo("Arrival")))
    }
}
