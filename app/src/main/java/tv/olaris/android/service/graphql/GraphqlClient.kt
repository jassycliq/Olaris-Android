package tv.olaris.android.service.graphql

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.auth0.android.jwt.JWT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databases.Server

class GraphqlClient(val server: Server) {
     suspend fun get(): ApolloClient {
         // TODO: This should not be here
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
                builder.header("Authorization", "Bearer ${server.currentJWT}")
                chain.proceed(builder.build())
            }
            .build()

        return ApolloClient.builder()
            .serverUrl("${server.url}/olaris/m/query")
            .okHttpClient(okHttpClient)
            .build()
    }

}