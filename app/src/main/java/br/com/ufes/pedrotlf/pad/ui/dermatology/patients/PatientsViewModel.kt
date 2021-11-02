package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.util.Log
import androidx.lifecycle.*
import br.com.ufes.pedrotlf.pad.SingleLiveEvent
import br.com.ufes.pedrotlf.pad.data.SadeRepository
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO,
    private val sadeRepository: SadeRepository,
) : ViewModel() {

    val patients = patientsDAO.getPatients().asLiveData()

    private val _sendPatientRequest = SingleLiveEvent<Resource<Nothing?>>()
    val sendPatientRequest: LiveData<Resource<Nothing?>> = _sendPatientRequest

    fun sendPatientsToServer(): Boolean {
        val patientsList = patients.value
        return if(!patientsList.isNullOrEmpty()){
            sendAllPatients(patientsList)
            true
        } else {
            false
        }
    }

    private fun sendAllPatients(patientsList: List<PatientDTO>) = viewModelScope.launch{
        _sendPatientRequest.value = Resource.Loading
        patientsList.forEach { patient ->
            runCatching {
                sadeRepository.sendDermatologyPatientLesion(
                    patient.patientData,
                    patient.lesions.first()
                )
            }.onSuccess {
                patientsDAO.delete(patient)
            }.onFailure {
                _sendPatientRequest.value = Resource.Failure(it)
                return@launch
            }
        }
        _sendPatientRequest.value = Resource.Success(null)
    }

    fun launchDeletePatient(patient: PatientDTO) = viewModelScope.launch {
        patientsDAO.delete(patient)
    }
}