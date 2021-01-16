package tv.olaris.android.repositories

import tv.olaris.android.databases.Server
import tv.olaris.android.databases.ServerDoa

class ServersRepository(serverDoa: ServerDoa) {
    val serverDoa : ServerDoa = serverDoa

    suspend fun insertServer(server: Server){
        serverDoa.insertServer(server)
    }
}