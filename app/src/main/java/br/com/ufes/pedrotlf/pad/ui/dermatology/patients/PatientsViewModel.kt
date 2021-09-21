package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO,
) : ViewModel() {

    val patients = patientsDAO.getPatients().asLiveData()

    fun deletePatient(patient: PatientDTO) = viewModelScope.launch {
        patient.lesions.forEach {
            it.images.forEach { img -> patientsDAO.delete(img) }
            patientsDAO.delete(it.lessionData)
        }
        patientsDAO.delete(patient.patientData)
    }
}