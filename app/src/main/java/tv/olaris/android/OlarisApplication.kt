package tv.olaris.android

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tv.olaris.android.databases.Server
import tv.olaris.android.databases.ServerDatabase
import tv.olaris.android.repositories.OlarisGraphQLRepository
import tv.olaris.android.repositories.ServersRepository
import tv.olaris.android.service.http.OlarisHttpService
import java.net.ConnectException
import java.util.*

@HiltAndroidApp
class OlarisApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ServerDatabase.getDatabase(this, applicationScope) }

    private val _graphlClients: MutableMap<Int, OlarisGraphQLRepository> = mutableMapOf()

    // TODO: Is this an ok way of doing this?
    val serversRepository by lazy { ServersRepository(database.serverDoa()) }

    suspend fun getOrInitRepo(serverId: Int): OlarisGraphQLRepository {
        if (!_graphlClients.containsKey(serverId)) {
            _graphlClients[serverId] =
                OlarisGraphQLRepository(serversRepository.getServerById(serverId))
        }

        return _graphlClients.getValue(serverId)
    }

    suspend fun checkServerStatus() {
        serversRepository.allServers.collect {
            for (server in it) {
                checkServer(server)
            }
        }
    }

    suspend fun checkServer(server: Server): Boolean {
        try {
            val version = OlarisHttpService(server.url).GetVersion()

            if (version != server.version || !server.isOnline) {
                server.version = version
                if (!server.isOnline) {
                    server.isOnline = true
                }
                serversRepository.updateServer(server)
            }
            return true

        } catch (e: ConnectException) {
            if (server.isOnline) {
                server.isOnline = false
                serversRepository.updateServer(server)
            }
            return false
        }
        return false
    }

    init {
        instance = this

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Log.d("ASD", "run: checking")

                 applicationScope.launch {
                    checkServerStatus()
                }
            }
        }, 0, 10000)
    }

    companion object {
        private var instance: OlarisApplication? = null

        fun applicationContext(): OlarisApplication {
            return instance as OlarisApplication
        }
    }
}