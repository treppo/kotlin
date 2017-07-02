package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class YorckTest {
    @Test
    fun extractNothingFromEmptyHtml() {
        val html = ""

        val actualResults = AsyncYorck("url", HttpMock.getAsync(html)).getInfos().join()

        assertThat(actualResults, equalTo(listOf()))
    }

    @Test
    fun extractMovieTitlesFromHtml() {
        val html = javaClass.getResource("/yorck-response.html").readText()

        val actualResults = AsyncYorck("url", HttpMock.getAsync(html)).getInfos().join()

        assertThat(actualResults, equalTo(listOf(Result("Arrival"), Result("Alien Covenant"))))
    }

    @Test
    fun properlyParsesUTF8Characters() {
        val html = javaClass.getResource("/yorck-response-utf8-title.html").readText()

        val actualResults = AsyncYorck("url", HttpMock.getAsync(html)).getInfos().join()

        assertThat(actualResults, equalTo(listOf(Result("ÖÜÄöüä"))))
    }
}
