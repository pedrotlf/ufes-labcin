package br.com.ufes.pedrotlf.pad.ui

import androidx.lifecycle.ViewModel
import br.com.ufes.pedrotlf.pad.MyPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PadHomeViewModel @Inject constructor(
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
}