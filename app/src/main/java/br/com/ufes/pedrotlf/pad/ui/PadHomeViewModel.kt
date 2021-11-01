package br.com.ufes.pedrotlf.pad.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.MyPrefs
import br.com.ufes.pedrotlf.pad.api.DermatologyApi
import br.com.ufes.pedrotlf.pad.data.DermatologyRepository
import br.com.ufes.pedrotlf.pad.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PadHomeViewModel @Inject constructor(
    private val repository: DermatologyRepository,
    private val prefs: MyPrefs
): ViewModel() {

    private val serverUrl: String get() {
        return prefs.serverUrl
    }

    val serverIpAndPort: List<String> get(){
        return serverUrl.replace("http:", "").replace("/", "").split(":")
    }

    fun changeServerUrl(newUrl: String){
        prefs.serverUrl = newUrl
    }

    private val _connectionStatus = MutableLiveData<Resource<Nothing?>>()
    val connectionStatus: LiveData<Resource<Nothing?>> = _connectionStatus

    fun testConnection() = viewModelScope.launch {
        _connectionStatus.value = Resource.Loading
        runCatching {
            repository.testConnection(serverUrl)
        }.onSuccess {
            _connectionStatus.value = Resource.Success(null)
        }.onFailure {
            _connectionStatus.value = Resource.Failure(it)
        }
    }

    init {
        testConnection()
    }
}