package tv.olaris.android.repositories

import AllMoviesQuery
import FindMovieQuery
import android.util.Log
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
    fun createApolloClient() : ApolloClient{
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hcmFuIiwidXNlcl9pZCI6MSwiYWRtaW4iOnRydWUsImV4cCI6MTYxMDQ2MTk0NywiaXNzIjoiYnNzIn0.RrMye4uC6jWSR0FUQOnji_OxXLUy4CaJV4URfcRuvcA"
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

    suspend fun findMovieByUUID(uuid: String): Movie?  = withContext(Dispatchers.IO) {
        var movie : Movie? = null
        Log.d("uuid2", uuid)
        try {
            val res = createApolloClient().query(FindMovieQuery(uuid = uuid)).await()
            Log.d("uuid2", res.toString())
            if(res.data != null && res.data?.movies != null){
                var m = res.data!!.movies!!.first()!!
                Log.d("uuid2", m.toString())
                movie = Movie(m.title, m.uuid, m.year.toInt(), m.overview, m.posterURL)
            }

        } catch(e: ApolloException){
            println("BAH $e")
        }

        return@withContext movie
        /*
        return@withContext getAllMovies().find {
            it.uuid == uuid
        }*/
    }

    suspend fun getAllMovies() : List<Movie> = withContext(Dispatchers.IO){
        var movies : MutableList<Movie> = mutableListOf()

        try {
           val res = createApolloClient().query(AllMoviesQuery()).await()

            if(res.data != null && res.data?.movies != null){
                for(movie in res.data!!.movies){
                    val m = movie!!
                    movies.add(Movie(m.title, m.uuid, m.year.toInt(), m.overview, m.posterURL))
                }
                return@withContext movies.toList()
            }
        } catch(e: ApolloException){
          println("BAH $e")
        }

        return@withContext movies
        /*
        listOf(
                Movie("The Matrix", "1234", 1999, "Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"),
                Movie("The Matrix Reloaded", "1235", 2003, "Second, not so Awesome,  movie about Neo", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/aA5qHS0FbSXO8PxcxUIHbDrJyuh.jpg"),
                Movie("Weekend at Bernies", "1236", 1989, "Two friends are invited for a weekend to a luxury island with their boss. The boss gets shot and nobody seems to notice, except for the two friends. In order not to become suspects of murder they treat the body as a puppet and make people believe he's still alive. The killer wants to do his job so when he is informed that the stiff is still alive he's got to shoot him again, and again, and again.", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/ym09EHiQYtwYnLqTv38KMjwwabc.jpg"),
                Movie("Pretty Woman", "1237", 1990, "When a millionaire wheeler-dealer enters a business contract with a Hollywood hooker Vivian Ward, he loses his heart in the bargain. ", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hMVMMy1yDUvdufpTl8J8KKNYaZX.jpg")
        )*/
    }
}
