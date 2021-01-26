package tv.olaris.android.models

import kotlin.math.roundToInt

class Movie(uuid: String, val backdropPath: String, posterUrl: String,  val title: String, val year: Int, val overview: String, val movieBase: fragment.MovieBase) : MediaItem(
    uuid = uuid,
    posterUrl = posterUrl,
    name = title
) {
    var fileUUIDs : MutableList<String> = mutableListOf()

    companion object {
        fun createFromGraphQLMovieBase(m: fragment.MovieBase) : Movie{
            val movie = Movie(title = m.title, year = m.year.toInt(), overview =  m.overview, movieBase =  m, uuid = m.uuid, backdropPath = m.backdropPath, posterUrl = m.posterURL)
            if (m.files.isNotEmpty()) {
                for (file in m.files) {
                    if (file != null) {
                        movie.fileUUIDs.add(file.uuid)
                    }
                }
            }
            movie.fileUuid = movie.fileUUIDs.first()

            if(m.files.isNotEmpty()) {
                movie.runtime = m.files.first()!!.totalDuration!!
            }
            movie.playtime = m.playState?.fragments?.playstateBase?.playtime!!
            movie.subTitle = movie.year.toString()
            return movie
        }
    }

    fun getFileName() : String {
        val file = movieBase.files.first()
        if(file != null){
            return file.fileName
        }
        return "Unknown"
    }

    fun getRuntime() : Int {
        val file = movieBase.files.first()
        if(file != null){
           val minutes = (file.totalDuration!! / 60).roundToInt()
            return minutes
        }

        return 0
    }

    fun fullCoverArtUrl(baseUrl: String?) : String {
        return if(baseUrl != null) {
            "${baseUrl}/olaris/m/images/tmdb/w1280/${this.backdropPath}"
        }else{
            ""
        }
    }

    fun getResolution(): String {
        val file = this.movieBase.files.first()
        if(file != null && file.streams.isNotEmpty()) {
            val videoStreams = file.streams.filter{
                if(it != null){
                    it.fragments.streamBase.streamType == "video"
                }else{
                    false
                }
            }
            if(videoStreams.isNotEmpty()){
               return videoStreams.first()?.fragments?.streamBase?.resolution.toString()
            }

        }
        return "unknown"
    }
}

