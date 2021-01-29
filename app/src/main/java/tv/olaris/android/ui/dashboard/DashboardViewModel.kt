package tv.olaris.android.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databases.Server
import tv.olaris.android.models.MediaItem
import tv.olaris.android.repositories.OlarisGraphQLRepository

class DashboardViewModel : ViewModel() {
    private var _continueWatchingItems = MutableLiveData<List<MediaItem>>()
    val continueWatchingItems: LiveData<List<MediaItem>> =
        _continueWatchingItems!!

    private var _recentlyAddedItems = MutableLiveData<List<MediaItem>>()
    val recentlyAddedItems: LiveData<List<MediaItem>> =
        _recentlyAddedItems!!

    private var _server = MutableLiveData<Server>()
    val server: LiveData<Server> = _server

    fun loadData(serverId: Int) = viewModelScope.launch {
        Log.d("dashboard", "Server id $serverId")

        if(_recentlyAddedItems.value.isNullOrEmpty() || _continueWatchingItems.value.isNullOrEmpty()){
            val s = OlarisApplication.applicationContext().serversRepository.getServerById(serverId)
            _server.value = s

            _continueWatchingItems.value = OlarisGraphQLRepository(s).findContinueWatchingItems()
            Log.d("dashboard", _continueWatchingItems.value?.size.toString())

            _recentlyAddedItems.value = OlarisGraphQLRepository(s).findRecentlyAddedItems()
            Log.d("dashboard", _recentlyAddedItems.value?.size.toString())
        }

    }
}