package tv.olaris.android.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddServerViewModel: ViewModel(){
      var serverUrl = MutableLiveData<String>()
      var username = MutableLiveData<String>()
      var password = MutableLiveData<String>()

}