package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.data.dto.LesionDataDTO
import br.com.ufes.pedrotlf.pad.data.dto.LesionImageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientLesionPageViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO
) : ViewModel() {

    val currentLesion = MutableLiveData<LesionDTO?>()

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

    private fun attachImage(path: String) = viewModelScope.launch{
        currentLesion.value?.let {
            val newImage = LesionImageDTO(path, it.lessionData.id)
            val imageId = patientsDAO.insert(newImage)

            val currentList = _imageList.value?.toMutableList() ?: mutableListOf()
            currentList.add(newImage.copy(id = imageId.toInt()))
            _imageList.value = currentList
        }
    }

    fun deleteImage(image: LesionImageDTO) = viewModelScope.launch {
        if(patientsDAO.delete(image) > 0){
            val currentList = _imageList.value?.toMutableList() ?: mutableListOf()
            currentList.remove(image)
            _imageList.value = currentList
        }
    }

    private val _imageList = MutableLiveData<List<LesionImageDTO>>()
    val imageList: LiveData<List<LesionImageDTO>> = _imageList
    val currentImagePath = MutableLiveData<String?>()

    fun confirmImagePath(){
        val currentImagePath = currentImagePath.value
        currentImagePath?.let {
            attachImage(it)
        }
    }

    fun setInitialImagePathList(list: List<LesionImageDTO>){
        _imageList.value = list
    }
}