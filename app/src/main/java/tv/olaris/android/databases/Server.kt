package tv.olaris.android.databases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Server constructor(val url: String, val username: String, val password: String, val currentJWT: String, @PrimaryKey val id: Int=0)

interface  ServerDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServer(server: Server)

    @get:Query("select * from Server")
    val serverLiveData: LiveData<Server?>
}

abstract class ServerDatabase: RoomDatabase(){
    abstract val serverDoa: ServerDoa
}

private lateinit var INSTANCE: ServerDatabase

fun getDatabase(context: Context): ServerDatabase{
    synchronized(ServerDatabase::class){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext, ServerDatabase::class.java, "servers_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}