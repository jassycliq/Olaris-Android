package tv.olaris.android.service.http

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import tv.olaris.android.service.http.model.LoginRequest
import tv.olaris.android.service.http.model.LoginResponse
import java.net.ConnectException

class OlarisHttpService(val baseUrl: String) {
    suspend fun LoginUser(username: String, password: String) : LoginResponse {
        val authLoginUrl = baseUrl + "/olaris/m/v1/auth"
        Log.d("http", "Login URL: $authLoginUrl")
        try{
            val client = HttpClient(Android) {
                install(JsonFeature){
                    accept(ContentType.Application.Json)
                    serializer = KotlinxSerializer()
                }
                expectSuccess = false
            }
            val loginResponse = client.post<LoginResponse>(authLoginUrl){
                body = LoginRequest(username, password)
                contentType(ContentType.Application.Json)
            }
            Log.d("http", "Login response $loginResponse")
            return loginResponse
        }catch(e: ClientRequestException){
            Log.d("http", "Error: ${e.message}")
            return LoginResponse(true, e.toString())
        }catch(e: ConnectException){
            return LoginResponse(true, e.toString())
        }
    }
}