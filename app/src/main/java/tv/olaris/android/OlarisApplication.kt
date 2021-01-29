package tv.olaris.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import tv.olaris.android.databases.ServerDatabase
import tv.olaris.android.repositories.OlarisGraphQLRepository
import tv.olaris.android.repositories.ServersRepository

@HiltAndroidApp
class OlarisApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ServerDatabase.getDatabase(this, applicationScope) }

    private val _graphlClients: MutableMap<Int,OlarisGraphQLRepository> = mutableMapOf()

    // TODO: Is this an ok way of doing this?
    val serversRepository by lazy { ServersRepository(database.serverDoa()) }

    suspend fun getOrInitRepo(serverId: Int) : OlarisGraphQLRepository {
        if(!_graphlClients.containsKey(serverId)){
            _graphlClients[serverId] = OlarisGraphQLRepository(serversRepository.getServerById(serverId))
        }

        return _graphlClients.getValue(serverId)
    }

    init {
        instance = this
    }

    companion object {
        private var instance: OlarisApplication? = null

        fun applicationContext() : OlarisApplication {
            return instance as OlarisApplication
        }
    }
}