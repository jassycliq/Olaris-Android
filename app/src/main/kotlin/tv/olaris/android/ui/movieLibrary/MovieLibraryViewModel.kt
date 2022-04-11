package tv.olaris.android.ui.movieLibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.models.Movie

class MovieLibraryViewModel : ViewModel() {
    private var _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private var _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean> = _dataLoaded

    fun loadData(serverId: Int) = viewModelScope.launch {
        _movies.value = OlarisApplication.applicationContext().getOrInitRepo(serverId).getAllMovies()
        _dataLoaded.value = true
    }
}