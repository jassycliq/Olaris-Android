package tv.olaris.android.ui.movieDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.models.Movie
import tv.olaris.android.repositories.MoviesRepository

class MovieDetailsViewModel() : ViewModel() {

    private var _movie = MutableLiveData<Movie?>()
    var movie: MutableLiveData<Movie?> = _movie

    private val _posterUrl = MutableLiveData<String>()
    val posterUrl: LiveData<String> = _posterUrl

    private val _coverArtUrl = MutableLiveData<String>()
    val coverArtUrl: LiveData<String> = _coverArtUrl


    fun getMovie(server_id: Int, uuid: String){
        if(_movie.value == null){
            viewModelScope.launch {
                val server = OlarisApplication.applicationContext().serversRepository.getServerById(server_id)
                val moviesRepository = MoviesRepository(server)
                _movie.value =  moviesRepository!!.findMovieByUUID(uuid)

                _posterUrl.value = _movie.value?.fullPosterUrl(server.url)
                _coverArtUrl.value = _movie.value?.fullCoverArtUrl(server.url)

            }
        }

    }
}