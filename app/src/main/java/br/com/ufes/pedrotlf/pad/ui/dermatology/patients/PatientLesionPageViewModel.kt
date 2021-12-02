package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.data.dto.LesionDataDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientLesionPageViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO
) : ViewModel() {

    val originalLesion = MutableLiveData<LesionDTO?>()

    var bodyRegion = ""
    var diagnostic = ""
    var diagnosticSecondary = ""
    var woundGrown: Boolean? = null
    var woundItched: Boolean? = null
    var woundBleed: Boolean? = null
    var woundHurt: Boolean? = null
    var woundPatternChanged: Boolean? = null
    var woundHasRelief: Boolean? = null

    private val _lesionUpdated = MutableLiveData<Int>()
    val lesionUpdated: LiveData<Int> = _lesionUpdated

    fun updateLesion(lesionData: LesionDataDTO) = viewModelScope.launch{
        val newLesion = LesionDataDTO(
            bodyRegion, diagnostic, diagnosticSecondary,
            woundGrown == true,
            woundItched == true,
            woundBleed == true,
            woundHurt == true,
            woundPatternChanged == true,
            woundHasRelief == true,
            lesionData.patientId,
            lesionData.id
        )
        _lesionUpdated.value = patientsDAO.update(
            newLesion
        )
    }

    private val _lesionCreated = MutableLiveData<LesionDTO>()
    val lesionCreated: LiveData<LesionDTO> = _lesionCreated

    fun attachLesion(patientId: Int) = viewModelScope.launch {
        val newLesion = LesionDataDTO(
            bodyRegion, diagnostic, diagnosticSecondary,
            woundGrown == true,
            woundItched == true,
            woundBleed == true,
            woundHurt == true,
            woundPatternChanged == true,
            woundHasRelief == true,
            patientId
        )
        val lesionId = patientsDAO.insert(newLesion)
        if(lesionId > 0)
            _lesionCreated.value = LesionDTO(newLesion.copy(id = lesionId.toInt()), emptyList())
    }
}