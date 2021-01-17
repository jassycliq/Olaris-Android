package tv.olaris.android.models

import android.util.Log
import tv.olaris.android.databases.Server
import kotlin.math.roundToInt

data class Movie(val title: String, val uuid: String, val year: Int, val overview: String, val posterUrl: String, val movieBase: fragment.MovieBase){
    var fileUUIDs : MutableList<String> = mutableListOf()

    companion object {
        fun createFromGraphQLMovieBase(m: fragment.MovieBase) : Movie{
            val movie = Movie(m.title, m.uuid, m.year.toInt(), m.overview, m.posterURL, m)

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

    fun fullPosterUrl(baseUrl: String) : String {
        return "${baseUrl}/${this.posterUrl}"
    }

    // TODO: Fix this on the server side
    fun fullCoverArtUrl(baseUrl: String) : String {
        return "${baseUrl}/olaris/m/images/tmdb/w1280/${this.movieBase.backdropPath.toString()}"
    }

}

