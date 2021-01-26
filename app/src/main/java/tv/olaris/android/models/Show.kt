package tv.olaris.android.models

import android.util.Log
import fragment.SeasonBase

data class Show(val name: String, val overview: String, val backdropPath: String, val firstAirDate: String, val posterPath: String, val unwatchedEpisodeCount: Int, val uuid: String, val seriesBase: fragment.SeriesBase? = null){
    var seasons : MutableList<Season> = mutableListOf()

    companion object {
        fun buildSeason(seasonBase: SeasonBase): Season?{
            var season : Season? = null
            with(seasonBase) {
                season = Season(
                    name,
                    overview,
                    seasonNumber,
                    airDate,
                    posterPath,
                    uuid,
                    unwatchedEpisodesCount
                )

                for (episode in this.episodes) {
                    with(episode!!.fragments.episodeBase) {
                        val episode =
                            Episode(this, seasonBase)
                        for (file in this.files) {
                            with(file!!.fragments.fileBase) {
                                episode.files.add(
                                    File(
                                        fileName,
                                        filePath,
                                        uuid,
                                        totalDuration,
                                        fileSize,
                                        this
                                    )
                                )
                            }
                        }
                        if(season != null) {
                            season!!.episodes.add(episode)
                        }
                    }
                }
                season!!.episodes.sortBy { it.episodeNumber }
            }
            return season
        }
        fun createFromGraphQLSeries(m: AllSeriesQuery.Series) : Show{
            return Show(name = m.name, uuid = m.uuid,  overview = m.overview, posterPath =  m.posterPath, backdropPath = m.backdropPath, unwatchedEpisodeCount = m.unwatchedEpisodesCount, firstAirDate = m.firstAirDate)
        }

        fun createFromGraphQLSeriesBase(m: fragment.SeriesBase) : Show{
            val show = Show(name = m.name, uuid = m.uuid,  overview = m.overview, seriesBase = m, posterPath =  m.posterPath, backdropPath = m.backdropPath, unwatchedEpisodeCount = m.unwatchedEpisodesCount, firstAirDate = m.firstAirDate)
            for(s in m.seasons){
                with(s!!.fragments.seasonBase){
                    val season = buildSeason(this)
                    if(season != null) {
                        season.episodes.sortBy { it.episodeNumber }
                        show.seasons.add(season)
                    }
                }
            }
            show.seasons.sortBy { it.seasonNumber }
            return show
        }
    }

    fun fullPosterUrl(baseUrl: String) : String {
        return "${baseUrl}/olaris/m/images/tmdb/w780/${this.posterPath}"
    }

    // TODO: Fix this on the server side
    fun fullCoverArtUrl(baseUrl: String) : String {
        return "${baseUrl}/olaris/m/images/tmdb/w1280/${this.backdropPath.toString()}"
    }
}
