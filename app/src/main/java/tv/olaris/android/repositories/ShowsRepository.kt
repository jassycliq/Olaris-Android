package tv.olaris.android.repositories

import AllSeriesQuery
import FindSeriesQuery
import android.util.Log
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import tv.olaris.android.databases.Server
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
                    Log.d("shows", "Adding show ${m.name}")
                    shows.add(Show.createFromGraphQLSeries(m))
                }
            }
        } catch(e: ApolloException){
            logException(e)
        }
        return shows
    }

    suspend fun findShowByUUID(uuid: String) : Show?{
        try {
            val res = GraphqlClient(server).get().query(FindSeriesQuery(uuid)).await()
            if(res.data != null && res.data!!.series.isNotEmpty()){
                return Show.createFromGraphQLSeriesBase(res.data!!.series.first()!!.fragments.seriesBase)
            }
        }   catch(e: ApolloException) {
            logException(e)
        }
        return null
    }

    private fun logException(e: ApolloException){
        Log.e("apollo", "Error getting movies: ${e.localizedMessage}")
        Log.e("apollo", "Cause: ${e.cause}")
        Log.e("apollo", e.toString())
    }
}