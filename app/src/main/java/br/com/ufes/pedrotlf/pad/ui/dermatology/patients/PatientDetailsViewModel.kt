package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.*
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDataDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDetailsViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val patient = stateHandle.get<PatientDTO>("patient")

    var susNumber = patient?.patientData?.susNumber
    var age = patient?.patientData?.age
    var serviceCity = patient?.patientData?.serviceCity
    var livingCity = patient?.patientData?.livingCity

    private val _patientUpdated = MutableLiveData<Int>()
    val patientUpdated: LiveData<Int> = _patientUpdated

    fun updatePatient() = viewModelScope.launch{
        patient?.patientData?.let {
            _patientUpdated.value = patientsDAO.update(
                PatientDataDTO(
                    susNumber ?: "",
                    age ?: 0,
                    serviceCity ?: "",
                    livingCity ?: "",
                    it.id
                )
            )
        }
    }
}