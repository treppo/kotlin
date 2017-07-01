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

fun <T> withMockServer(block: (WireMockServer) -> T): T {
    val mockServer = WireMockServer(wireMockConfig().dynamicPort())
    val resource = block.javaClass.getResource("/yorck-response.html").readText()
    mockServer.stubFor(get("/yorck").willReturn(ok(resource)))
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
            val yorckUrl = "http://localhost:${mockServer.port()}/yorck"
            withTestApplication(Application(Configuration(yorckUrl))::module.get()) {
                with(handleRequest(HttpMethod.Get, "/")) {
                    assert.that(response.status(), equalTo(HttpStatusCode.OK))
                    assert.that(response.content!!, containsSubstring("Arrival"))
                    assert.that(response.content!!, containsSubstring("Alien Covenant"))
                }
            }
        }
    }
}
