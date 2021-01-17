package tv.olaris.android.repositories

import AllMoviesQuery
import CreateStreamingTicketMutation
import FindMovieQuery
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
import tv.olaris.android.models.Movie
import tv.olaris.android.service.http.OlarisHttpService
import javax.inject.Inject

class MoviesRepository @Inject constructor(server: Server){
    private var server = server

    // TODO: Refactor so it can be used by other repos in the future.
    private suspend fun createApolloClient(baseUrl: String, token: String): ApolloClient {
        val j = JWT(server.currentJWT)

        Log.d("jwt", j.expiresAt.toString())
        if(j.isExpired(10)) {
            OlarisApplication.applicationContext().serversRepository.refreshJwt(server.id)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val builder: Request.Builder =
                    original.newBuilder().method(original.method(), original.body())
                builder.header("Authorization", "Bearer $token")
                chain.proceed(builder.build())
            }
            .build()

        return ApolloClient.builder()
            .serverUrl("${baseUrl}/olaris/m/query")
            .okHttpClient(okHttpClient)
            .build()
    }

    suspend fun getStreamingUrl(uuid: String): String? {
        val m = CreateStreamingTicketMutation(uuid = uuid)
        val res = createApolloClient(server.url, server.currentJWT).mutate(m).await()

        if (res.data != null && res.data?.createStreamingTicket != null){
            return "${server.url}/${res.data!!.createStreamingTicket.dashStreamingPath}"
        }

        return null
    }

    suspend fun findMovieByUUID(uuid: String): Movie?  = withContext(Dispatchers.IO) {
        var movie : Movie? = null
        try {
            val res = createApolloClient(server.url, server.currentJWT).query(FindMovieQuery(uuid = uuid)).await()
            if(res.data != null && res.data?.movies != null){
                var m = res.data!!.movies.first()!!
                movie = Movie.createFromGraphQLMovieBase(m.fragments.movieBase)
            }

        } catch(e: ApolloException){
            logException(e)
        }

        return@withContext movie
    }

    suspend fun getAllMovies() : List<Movie> = withContext(Dispatchers.IO){
        var movies : MutableList<Movie> = mutableListOf()

        try {
           val res = createApolloClient(server.url, server.currentJWT).query(AllMoviesQuery()).await()

            if(res.data != null && res.data?.movies != null){
                for(movie in res.data!!.movies){
                    val m = movie!!
                    movies.add(Movie.createFromGraphQLMovieBase(m.fragments.movieBase))
                }
                return@withContext movies.toList()
            }
        } catch(e: ApolloException){
            logException(e)
        }

        return@withContext movies
    }

    private fun logException(e: ApolloException){
        println("Error getting movies: ${e.localizedMessage}")
        println("Cause: ${e.cause}")
        println("Message: ${e.message}")
    }
}
