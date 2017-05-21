package yorck_ratings

import org.jsoup.Jsoup

object YorckInfoParser {
    fun parse(html: String): List<YorckInfo> =
            Jsoup
                    .parse(html)
                    .select(".movie-info .movie-details h2")
                    .map { element -> YorckInfo(element.text()) }
}
