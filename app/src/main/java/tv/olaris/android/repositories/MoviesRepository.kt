package tv.olaris.android.repositories

import AllMoviesQuery
import CreateStreamingTicketMutation
import FindMovieQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import tv.olaris.android.models.Movie
import javax.inject.Inject

class MoviesRepository @Inject constructor(){
    // TODO: Refactor so it can be used by other repos in the future.
    fun createApolloClient() : ApolloClient{
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hcmFuIiwidXNlcl9pZCI6MSwiYWRtaW4iOnRydWUsImV4cCI6MTYxMDU1MzYxMCwiaXNzIjoiYnNzIn0.JAF25IgXiqML39y7QVoezFWsywbDpukqy9i6FOaCfSY"
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain: Interceptor.Chain ->
                    val original: Request = chain.request()
                    val builder: Request.Builder = original.newBuilder().method(original.method(), original.body())
                    builder.header("Authorization", "Bearer $token")
                    chain.proceed(builder.build())
                }
                .build()

        return ApolloClient.builder()
                .serverUrl("http://192.168.178.64:4321/olaris/m/query")
                .okHttpClient(okHttpClient)
                .build()
    }

    suspend fun getStreamingUrl(uuid: String): String? {
        val m = CreateStreamingTicketMutation(uuid = uuid)
        val res = createApolloClient().mutate(m).await()

        if (res.data != null && res.data?.createStreamingTicket != null){
            return "http://192.168.178.64:4321/${res.data!!.createStreamingTicket!!.dashStreamingPath}"
        }

        return null
    }

    suspend fun findMovieByUUID(uuid: String): Movie?  = withContext(Dispatchers.IO) {
        var movie : Movie? = null
        try {
            val res = createApolloClient().query(FindMovieQuery(uuid = uuid)).await()
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
           val res = createApolloClient().query(AllMoviesQuery()).await()

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
