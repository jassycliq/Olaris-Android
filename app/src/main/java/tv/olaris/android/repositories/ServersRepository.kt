package tv.olaris.android.repositories

import kotlinx.coroutines.flow.Flow
import tv.olaris.android.databases.Server
import tv.olaris.android.databases.ServerDoa
import tv.olaris.android.service.http.OlarisHttpService
import tv.olaris.android.service.http.model.LoginResponse

class ServersRepository(private val serverDoa: ServerDoa) {
    val allServers: Flow<List<Server>> = serverDoa.getServers()

    suspend fun insertServer(server: Server){
        serverDoa.insertServer(server)
    }

    suspend fun getServerById(id: Int): Server {
       return serverDoa.getServerById(id)
    }

    suspend fun refreshJwt(serverId: Int){
        val server = getServerById(serverId)
        val result : LoginResponse = OlarisHttpService(server.url).loginUser(server.username,server.password)
        if(!result.hasError && result.jwt != null){
            server.currentJWT = result.jwt
        }
        updateServer(server)

    }
    suspend fun updateServer(server: Server) {
        serverDoa.update(server)
    }

    fun getServerCount(): Int {
        return serverDoa.serverCount
    }

    suspend fun deleteServer(server: Server) {
        serverDoa.delete(server)
    }
}