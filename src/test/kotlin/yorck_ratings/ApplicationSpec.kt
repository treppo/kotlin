package yorck_ratings

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.testing.handleRequest
import org.jetbrains.ktor.testing.withTestApplication
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object ApplicationSpec : Spek({
    describe("the main application") {
        on("request to root path") {
            it("responds with a message") {
                withTestApplication(Application::module) {
                    with(handleRequest(HttpMethod.Get, "/")) {
                        assert.that(response.status(), equalTo(HttpStatusCode.OK))
                        assert.that(response.content, equalTo("Hello, world!"))
                    }
                }
            }
        }
    }
})
