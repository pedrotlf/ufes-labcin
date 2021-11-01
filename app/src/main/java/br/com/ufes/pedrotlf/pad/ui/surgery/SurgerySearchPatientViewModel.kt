package br.com.ufes.pedrotlf.pad.ui.surgery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.SingleLiveEvent
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.SadeRepository
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurgerySearchPatientViewModel @Inject constructor(
    private val sadeRepository: SadeRepository
): ViewModel() {

    val susNumber = MutableLiveData<String>()

    private val _patientRequest = SingleLiveEvent<Resource<SurgeryPatientDTO>>()
    val patientRequest: LiveData<Resource<SurgeryPatientDTO>> = _patientRequest

    fun searchPatient(susNumber: String) = viewModelScope.launch {
        _patientRequest.value = Resource.Loading
        runCatching {
            sadeRepository.getSurgeryPatient(susNumber)
        }.onSuccess {
            _patientRequest.value = Resource.Success(it)
        }.onFailure {
            _patientRequest.value = Resource.Failure(it)
        }
    }
}