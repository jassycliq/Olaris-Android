package tv.olaris.android.models

data class Episode(val name: String,
                   val overview: String,
                   val stillPath: String,
                   val airDate: String,
                   val episodeNumber: Int,
                   val uuid: String) {

    val files: MutableList<File> = mutableListOf()

    fun stillPathUrl(baseUrl: String) : String{
        return "${baseUrl}/olaris/m/images/tmdb/w300/${stillPath}"
    }
}