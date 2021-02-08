package tv.olaris.android.service.graphql

import com.apollographql.apollo.ApolloClient
import com.auth0.android.jwt.JWT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databases.Server

class GraphqlClient(var server: Server) {
    // TODO: This should probably be a singleton in the OlarisApplication
     suspend fun get(): ApolloClient {
        val j = JWT(server.currentJWT)

        if(j.isExpired(10)) {
            server = OlarisApplication.applicationContext().serversRepository.refreshJwt(server.id)
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