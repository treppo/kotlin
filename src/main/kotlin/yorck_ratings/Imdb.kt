package yorck_ratings

import org.jsoup.Jsoup

data class ImdbSearchInfo(val title: String)

interface Imdb {
    suspend fun getSearchInfo(title: String): ImdbSearchInfo?
}

class AsyncImdb(private val imdbSearchUrl: String) : Imdb {
    suspend override fun getSearchInfo(title: String): ImdbSearchInfo? =
            ImdbSearchParser.parse(AsyncHttp.get(imdbSearchUrl + title))
}

object ImdbSearchParser {
    fun parse(html: String): ImdbSearchInfo? =
            Jsoup
                    .parse(html)
                    .select(".poster .title a")
                    .map { ImdbSearchInfo(it.text()) }
                    .firstOrNull()
}
