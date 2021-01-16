package tv.olaris.android.models

import tv.olaris.android.databases.Server

data class Movie(val title: String, val uuid: String, val year: Int, val overview: String, val posterUrl: String){
    var fileUUIDs : MutableList<String> = mutableListOf()

    companion object {
        fun createFromGraphQLMovieBase(m: fragment.MovieBase) : Movie{
            val movie = Movie(m.title, m.uuid, m.year.toInt(), m.overview, m.posterURL)

            if (m.files.isNotEmpty()) {
                for (file in m.files) {
                    if (file != null) {
                        movie.fileUUIDs.add(file.uuid)
                    }
                }
            }
            return movie
        }
    }

    fun fullPosterPath(baseUrl: String) : String {
        return "${baseUrl}/${this.posterUrl}"
    }
}

