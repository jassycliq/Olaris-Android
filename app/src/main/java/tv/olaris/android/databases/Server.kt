package tv.olaris.android.databases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Entity(tableName = "servers")
data class Server constructor(val url: String, val username: String, val password: String, val name: String, val currentJWT: String, @PrimaryKey val id: Int=0)

@Dao
interface ServerDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServer(server: Server)

    @get:Query("select * from servers")
    val serverLiveData: LiveData<Server?>
}

@Database(entities = [Server::class], version = 1)
abstract class ServerDatabase: RoomDatabase(){
    abstract fun serverDoa(): ServerDoa
    companion object {
        private lateinit var INSTANCE: ServerDatabase

        fun getDatabase(context: Context,  scope: CoroutineScope): ServerDatabase {
            synchronized(ServerDatabase::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ServerDatabase::class.java,
                        "servers_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}