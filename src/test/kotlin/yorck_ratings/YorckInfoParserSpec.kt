package yorck_ratings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object YorckInfoParserSpec : Spek({
    describe("YorckInfoParser") {
        it("extracts no movie titles from empty html") {
            val emptyHtml = ""

            assertThat(YorckInfoParser.parse(emptyHtml), equalTo(listOf()))
        }

        it("extracts movie titles from the html") {
            val html = javaClass.getResource("/yorck-response.html").readText()

            assertThat(YorckInfoParser.parse(html), equalTo(listOf(YorckInfo("Arrival"), YorckInfo("Alien Covenant"))))
        }

        it("properly parses UTF-8 characters") {
            val html = javaClass.getResource("/yorck-response-utf8-title.html").readText()

            assertThat(YorckInfoParser.parse(html), equalTo(listOf(YorckInfo("ÖÜÄöüä"))))
        }
    }
})
