package tv.olaris.android.models

data class Show(val name: String, val overview: String, val backdropPath: String, val posterPath: String, val unwatchedEpisodeCount: Int, val uuid: String, val seriesBase: fragment.SeriesBase){
    companion object {
        fun createFromGraphQLSeriesBase(m: fragment.SeriesBase) : Show{
            return Show(name = m.name, uuid = m.uuid,  overview = m.overview, posterPath =  m.posterPath, seriesBase = m, backdropPath = m.backdropPath, unwatchedEpisodeCount = m.unwatchedEpisodesCount)

        }
    }

    fun fullPosterUrl(baseUrl: String) : String {
        return "${baseUrl}/olaris/m/images/tmdb/w780/${this.posterPath}"
    }

    // TODO: Fix this on the server side
    fun fullCoverArtUrl(baseUrl: String) : String {
        return "${baseUrl}/olaris/m/images/tmdb/w1280/${this.seriesBase.backdropPath.toString()}"
    }
}
