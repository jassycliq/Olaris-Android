package tv.olaris.android.databases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "servers")
data class Server constructor(val url: String, val username: String, val password: String, val name: String, val currentJWT: String, @PrimaryKey(autoGenerate = true) val id: Int=0)

@Dao
interface ServerDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServer(server: Server)

    @Query("select * from servers")
    fun getServers(): Flow<List<Server>>

    @get:Query("select count(*) from servers")
    val serverCount: Int

    @Query("select * from servers WHERE id = :id")
    suspend fun getServerById(id: Int): Server

    @Delete
    fun delete(model: Server)
}

@Database(entities = [Server::class], version = 2)
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