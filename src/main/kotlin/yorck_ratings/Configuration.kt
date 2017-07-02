package yorck_ratings

data class Configuration(val yorckUrl: String, val imdbSearchUrl: String, val imdbUrl: String)

fun productionConfiguration(): Configuration =
        Configuration(
                yorckUrl = "https://www.yorck.de/filme?filter_children=false&filter_subtitle=false&filter_today=true",
                imdbSearchUrl = "https://m.imdb.com/find?q=",
                imdbUrl = "https://m.imdb.com/")
