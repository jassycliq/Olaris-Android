package tv.olaris.android.repositories

import ContinueWatchingQuery
import RecentlyAddedQuery
import com.apollographql.apollo.coroutines.await
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Episode
import tv.olaris.android.models.MediaItem
import tv.olaris.android.models.Movie
import tv.olaris.android.service.graphql.GraphqlClient

class DashboardRepository(server: Server) {
    private var server = server

    // TODO: DRY these two methods up as they are essentially the same thing with a different GrapqhL class. Perhaps use GrapphQL interfaces here? Would need changes on the server side
    suspend fun findRecentlyAddedItems(): List<MediaItem> {
        val list = mutableListOf<MediaItem>()
        val res = GraphqlClient(server).get().query(RecentlyAddedQuery()).await()
        if (res.data != null && !res.data!!.recentlyAdded.isNullOrEmpty()) {
            for (item in res.data!!.recentlyAdded!!) {
                if (item!!.__typename == "Movie") {
                    val m = item!!.asMovie!!.fragments.movieBase
                    list.add(Movie.createFromGraphQLMovieBase(m) as MediaItem)
                } else if (item!!.__typename == "Episode") {
                    val m = item!!.asEpisode!!.fragments.episodeBase
                    list.add(
                        Episode(
                            m,
                            item!!.asEpisode!!.season?.fragments?.seasonBase
                        ) as MediaItem
                    )
                }
            }
        }
        return list
    }

    suspend fun findContinueWatchingItems(): List<MediaItem> {
        val list = mutableListOf<MediaItem>()
        val res = GraphqlClient(server).get().query(ContinueWatchingQuery()).await()
        if (res.data != null && !res.data!!.upNext.isNullOrEmpty()) {
            for (item in res.data!!.upNext!!) {
                if (item!!.__typename == "Movie") {
                    val m = item!!.asMovie!!.fragments.movieBase
                    list.add(Movie.createFromGraphQLMovieBase(m) as MediaItem)
                } else if (item!!.__typename == "Episode") {
                    val m = item!!.asEpisode!!.fragments.episodeBase
                    list.add(
                        Episode(
                            m,
                            item!!.asEpisode!!.season?.fragments?.seasonBase
                        ) as MediaItem
                    )
                }
            }
        }
        return list
    }
}