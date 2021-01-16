package tv.olaris.android.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import tv.olaris.android.databases.Server
import tv.olaris.android.databases.ServerDoa

class ServersRepository(serverDoa: ServerDoa) {
    val serverDoa : ServerDoa = serverDoa

    val allServers: Flow<List<Server>> = serverDoa.getServers()

    suspend fun insertServer(server: Server){
        serverDoa.insertServer(server)
    }

    suspend fun getServerById(id: Int): Server {
       return serverDoa.getServerById(id)
    }

    suspend fun getServerCount(): Int {
        return serverDoa.serverCount
    }

    suspend fun deleteServer(server: Server) {
        serverDoa.delete(server)
    }
}