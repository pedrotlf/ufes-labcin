package br.com.ufes.pedrotlf.pad.ui.surgery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.SadeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurgeryPatientDetailsViewModel @Inject constructor(
    private val sadeRepository: SadeRepository
): ViewModel(){

    private val _surgeonsRequest = MutableLiveData<Resource<List<String>>>()
    val surgeonsRequest: LiveData<Resource<List<String>>> = _surgeonsRequest

    private fun getSurgeonsList() = viewModelScope.launch {
        _surgeonsRequest.value = Resource.Loading
        runCatching {
            sadeRepository.getSurgeons()
        }.onSuccess {
            _surgeonsRequest.value = Resource.Success(it)
        }.onFailure {
            _surgeonsRequest.value = Resource.Failure(it)
        }
    }

    init {
        getSurgeonsList()
    }
}