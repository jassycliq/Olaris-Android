package tv.olaris.android.models

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

        /*
        fun createFromGraphQLMovie(m : AllMoviesQuery.Movie) : Movie{
            val movie = Movie(m.title, m.uuid,m.year.toInt(),m.overview,m.posterURL)
            if(m.files != null){
                for(file in m.files!!){
                    movie.fileUUIDs.add(file!!.uuid!!)
                }
            }
            return movie
        }

        fun createFromGraphQLMovie(m : FindMovieQuery.Movie) : Movie{
            val movie = Movie(m.title, m.uuid,m.year.toInt(),m.overview,m.posterURL)
            if(m.files != null){
                for(file in m.files!!){
                    movie.fileUUIDs.add(file!!.uuid!!)
                }
            }
            return movie
        }
        */
    }

    fun fullPosterPath() : String {
        return "http://192.168.178.64:4321/${this.posterUrl}"
    }
}

