package yorck_ratings

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.testing.handleRequest
import org.jetbrains.ktor.testing.withTestApplication
import org.junit.Test

class ApplicationTest {
    @Test
    fun showsResults() {
        withMockServer { mockServer: WireMockServer ->
            fun url(path: String = "") = "http://localhost:" + mockServer.port() + path
            val configuration = Configuration(
                    yorckUrl = "${url()}/yorck",
                    imdbSearchUrl = "${url()}/imdb-search?",
                    imdbUrl = url())
            withTestApplication(Application(configuration)::module.get()) {
                with(handleRequest(HttpMethod.Get, "/")) {
                    assertThat(response.status(), equalTo(HttpStatusCode.OK))
                    assertThat(response.content!!, containsSubstring(Result(yorckTitle = "Arrival", imdbTitle = "Arrival", imdbUrl = url("/title/tt2543164/?ref_=m_fn_al_1")).asListItem()))
                    assertThat(response.content!!, containsSubstring(Result(yorckTitle = "Alien Covenant", imdbTitle = "Alien: Covenant", imdbUrl = url("/title/tt2316204/?ref_=m_fn_al_4")).asListItem()))
                }
            }
        }
    }
}

private fun <T> withMockServer(block: (WireMockServer) -> T): T {
    fun readFile(file: String) = block.javaClass.getResource(file).readText()
    val mockServer = WireMockServer(wireMockConfig().dynamicPort())
    val yorckResponse = readFile("/yorck-response.html")
    val imdbSearchArrivalResponse = readFile("/imdb-search-arrival-response.html")
    val imdbSearchAlienResponse = readFile("/imdb-search-alien-response.html")
    val imdbDetailArrivalResponse = readFile("/imdb-detail-arrival-response.html")
    val imdbDetailAlienResponse = readFile("/imdb-detail-alien-response.html")

    mockServer.stubFor(get("/yorck").willReturn(ok(yorckResponse)))
    mockServer.stubFor(get("/imdb-search?Arrival").willReturn(ok(imdbSearchArrivalResponse)))
    mockServer.stubFor(get("/imdb-search?Alien%20Covenant").willReturn(ok(imdbSearchAlienResponse)))
    mockServer.stubFor(get("/title/tt2543164/?ref_=m_fn_al_1").willReturn(ok(imdbDetailArrivalResponse)))
    mockServer.stubFor(get("/title/tt2316204/?ref_=m_fn_al_4").willReturn(ok(imdbDetailAlienResponse)))

    mockServer.start()

    try {
        return block(mockServer)
    } finally {
        mockServer.stop()
    }
}
