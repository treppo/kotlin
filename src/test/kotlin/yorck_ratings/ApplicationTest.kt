package yorck_ratings

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.testing.handleRequest
import org.jetbrains.ktor.testing.withTestApplication
import org.junit.Test

private fun <T> withMockServer(block: (WireMockServer) -> T): T {
    fun readFile(file: String) = block.javaClass.getResource(file).readText()
    val mockServer = WireMockServer(wireMockConfig().dynamicPort())
    val yorckResponse = readFile("/yorck-response.html")
    val imdbSearchArrivalResponse = readFile("/imdb-search-arrival-response.html")
    val imdbSearchAlienResponse = readFile("/imdb-search-alien-response.html")

    mockServer.stubFor(get("/yorck").willReturn(ok(yorckResponse)))
    mockServer.stubFor(get("/imdb-search?Arrival").willReturn(ok(imdbSearchArrivalResponse)))
    mockServer.stubFor(get("/imdb-search?Alien%20Covenant").willReturn(ok(imdbSearchAlienResponse)))

    mockServer.start()

    try {
        return block(mockServer)
    } finally {
        mockServer.stop()
    }
}


class ApplicationTest {
    @Test
    fun ratingsAreShown() {
        withMockServer { mockServer: WireMockServer ->
            val configuration = Configuration(
                    yorckUrl = "http://localhost:${mockServer.port()}/yorck",
                    imdbSearchUrl = "http://localhost:${mockServer.port()}/imdb-search?")
            withTestApplication(Application(configuration)::module.get()) {
                with(handleRequest(HttpMethod.Get, "/")) {
                    assert.that(response.status(), equalTo(HttpStatusCode.OK))
                    assert.that(response.content!!, containsSubstring("Arrival • Arrival"))
                    assert.that(response.content!!, containsSubstring("Alien: Covenant • Alien Covenant"))
                }
            }
        }
    }
}
