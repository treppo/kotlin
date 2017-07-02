package yorck_ratings

import org.jsoup.Jsoup

object YorckParser {
    fun parse(html: String): List<Result> =
            Jsoup
                    .parse(html)
                    .select(".movie-info .movie-details h2")
                    .map { Result(yorckTitle = it.text()) }
}
