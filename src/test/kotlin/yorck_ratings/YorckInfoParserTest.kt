package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class YorckInfoParserTest {
    @Test
    fun extractNothingFromEmptyHtml() {
        val emptyHtml = ""

        assertThat(YorckInfoParser.parse(emptyHtml), equalTo(listOf()))
    }

    @Test
    fun extractMovieTitlesFromHtml() {
        val html = javaClass.getResource("/yorck-response.html").readText()

        assertThat(YorckInfoParser.parse(html), equalTo(listOf(YorckInfo("Arrival"), YorckInfo("Alien Covenant"))))
    }

    @Test
    fun properlyParsesUTF8Characters() {
        val html = javaClass.getResource("/yorck-response-utf8-title.html").readText()

        assertThat(YorckInfoParser.parse(html), equalTo(listOf(YorckInfo("ÖÜÄöüä"))))
    }
}
