package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class YorckParserTest {
    @Test
    fun extractNothingFromEmptyHtml() {
        val emptyHtml = ""

        assertThat(YorckParser.parse(emptyHtml), equalTo(listOf()))
    }

    @Test
    fun extractMovieTitlesFromHtml() {
        val html = javaClass.getResource("/yorck-response.html").readText()

        assertThat(YorckParser.parse(html), equalTo(listOf(Result("Arrival"), Result("Alien Covenant"))))
    }

    @Test
    fun properlyParsesUTF8Characters() {
        val html = javaClass.getResource("/yorck-response-utf8-title.html").readText()

        assertThat(YorckParser.parse(html), equalTo(listOf(Result("ÖÜÄöüä"))))
    }
}
