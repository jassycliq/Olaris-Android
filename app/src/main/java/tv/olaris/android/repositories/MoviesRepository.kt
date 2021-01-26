package tv.olaris.android.repositories

import AllMoviesQuery
import ContinueWatchingQuery
import CreatePlayStateMutation
import CreateStreamingTicketMutation
import FindMovieQuery
import RecentlyAddedQuery
import android.provider.MediaStore
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Episode
import tv.olaris.android.models.MediaItem
import tv.olaris.android.models.Movie
import tv.olaris.android.service.graphql.GraphqlClient
import tv.olaris.android.service.http.OlarisHttpService
import javax.inject.Inject
import com.apollographql.apollo.api.Response


class MoviesRepository @Inject constructor(server: Server) {
    private var server = server

    suspend fun updatePlayState(uuid: String, finished: Boolean, playtime: Double){
        Log.d("playstate", "$playtime.toString(), $uuid, $finished")
        val m = CreatePlayStateMutation(mediaFileUUID = uuid, finished = finished, playtime = playtime)
        GraphqlClient(server).get().mutate(m).await()
    }

    suspend fun getStreamingUrl(uuid: String): String? {
        val m = CreateStreamingTicketMutation(uuid = uuid)
        val res = GraphqlClient(server).get().mutate(m).await()

        if (res.data != null && res.data?.createStreamingTicket != null) {
            return "${server.url}${res.data!!.createStreamingTicket.dashStreamingPath}"
        }

        return null
    }

    suspend fun findMovieByUUID(uuid: String): Movie? = withContext(Dispatchers.IO) {
        var movie: Movie? = null
        try {
            val res = GraphqlClient(server).get().query(FindMovieQuery(uuid = uuid)).await()
            if (res.data != null && res.data?.movies != null) {
                var m = res.data!!.movies.first()!!
                movie = Movie.createFromGraphQLMovieBase(m.fragments.movieBase)
            }

        } catch (e: ApolloException) {
            logException(e)
        }

        return@withContext movie
    }

    suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        var movies: MutableList<Movie> = mutableListOf()

        try {
            val res = GraphqlClient(server).get().query(AllMoviesQuery()).await()

            if (res.data != null && res.data?.movies != null) {
                for (movie in res.data!!.movies) {
                    val m = movie!!
                    movies.add(Movie.createFromGraphQLMovieBase(m.fragments.movieBase))
                }
                return@withContext movies.toList()
            }
        } catch (e: ApolloException) {
            logException(e)
        }

        return@withContext movies
    }

    private fun logException(e: ApolloException) {
        println("Error getting movies: ${e.localizedMessage}")
        println("Cause: ${e.cause}")
        println("Message: ${e.message}")
    }
}
