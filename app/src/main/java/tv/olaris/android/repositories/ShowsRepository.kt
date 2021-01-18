package tv.olaris.android.repositories

import AllSeriesQuery
import android.util.Log
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import tv.olaris.android.databases.Server
import tv.olaris.android.models.Movie
import tv.olaris.android.models.Show
import tv.olaris.android.service.graphql.GraphqlClient

class ShowsRepository(val server: Server) {
    suspend fun getAllShows(): List<Show>{
        val shows : MutableList<Show> = mutableListOf()

        try {
            val res = GraphqlClient(server).get().query(AllSeriesQuery()).await()
            Log.d("shows", res.toString())

            if(res.data != null && res.data?.series != null){
                for(show in res.data!!.series){
                    val m = show!!
                    Log.d("shows", "Adding show ${m.fragments.seriesBase.name}")
                    shows.add(Show.createFromGraphQLSeriesBase(m.fragments.seriesBase))
                }
            }
        } catch(e: ApolloException){
            logException(e)
        }
        return shows
    }

    private fun logException(e: ApolloException){
        println("Error getting movies: ${e.localizedMessage}")
        println("Cause: ${e.cause}")
        println("Message: ${e.message}")
    }
}