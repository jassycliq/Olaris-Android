package tv.olaris.android.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import tv.olaris.android.databases.Server
import tv.olaris.android.databases.ServerDoa
import tv.olaris.android.service.olaris_http_api.OlarisHttpService
import tv.olaris.android.service.olaris_http_api.model.LoginResponse

class ServersRepository(serverDoa: ServerDoa) {
    val serverDoa : ServerDoa = serverDoa

    val allServers: Flow<List<Server>> = serverDoa.getServers()

    suspend fun insertServer(server: Server){
        serverDoa.insertServer(server)
    }

    suspend fun getServerById(id: Int): Server {
       return serverDoa.getServerById(id)
    }

    suspend fun refreshJwt(serverId: Int){
        val server = getServerById(serverId)
        val result : LoginResponse = OlarisHttpService(server.url).LoginUser(server.username,server.password)
        if(result.hasError && result.jwt != null){
            server.currentJWT = result.jwt!!
        }
        updateServer(server)

    }
    suspend fun updateServer(server: Server) {
        serverDoa.update(server)
    }
    suspend fun getServerCount(): Int {
        return serverDoa.serverCount
    }

    suspend fun deleteServer(server: Server) {
        serverDoa.delete(server)
    }
}