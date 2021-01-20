package tv.olaris.android.ui.mediaPlayer

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.repositories.MoviesRepository

class MediaPlayerViewModel(): ViewModel() {
    var serverId: Int = 0
    var uuid: String = ""

    private val streamingUrl: LiveData<String> = liveData{
        val s = OlarisApplication.applicationContext().serversRepository.getServerById(serverId)
        val moviesRepository = MoviesRepository(s)
        var streamingURL = moviesRepository.getStreamingUrl(uuid)
        if (streamingURL != null) {
            streamingURL += "?playableCodecs=avc1.640029&playableCodecs=avc1.64001f&playableCodecs=avc1.64001e&playableCodecs=avc1.640020&playableCodecs=avc1.640028&playableCodecs=avc1.640029&playableCodecs=avc1.64001f&playableCodecs=mp4a.40.2"
            emit(streamingURL)
        }
    }
    fun getStreamingUrl(s: Int, u: String): LiveData<String>{
        serverId = s
        uuid = u

        return streamingUrl
    }

}