package br.com.ufes.pedrotlf.pad.ui.dermatology.newpatient

import androidx.lifecycle.*
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import br.com.ufes.pedrotlf.pad.data.dto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPatientViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val susNumber = MutableLiveData<String?>()
    val age = MutableLiveData<Int?>()
    val serviceCity = MutableLiveData<String?>()
    val livingCity = MutableLiveData<String?>()

    val bodyRegion = MutableLiveData<String?>()
    val diagnostic = MutableLiveData<String?>()
    val diagnosticSecondary = MutableLiveData<String?>()
    val woundGrown = MutableLiveData<Boolean?>()
    val woundItched = MutableLiveData<Boolean?>()
    val woundBleed = MutableLiveData<Boolean?>()
    val woundHurt = MutableLiveData<Boolean?>()
    val woundPatternChanged = MutableLiveData<Boolean?>()
    val woundHasRelief = MutableLiveData<Boolean?>()

    private val _imagePathList = MutableLiveData<List<String>?>()
    val imagePathList: LiveData<List<String>?> = _imagePathList
    val currentImagePath = MutableLiveData<String?>()

    fun confirmImagePath(){
        val currentList = _imagePathList.value?.toMutableList() ?: mutableListOf()
        val currentImagePath = currentImagePath.value
        currentImagePath?.let {
            currentList.add(it)
            _imagePathList.value = currentList
        }
    }

    fun savePatientData() = viewModelScope.launch {
        val patientDataId = patientsDAO.insert(PatientDataDTO(
            susNumber.value ?: "",
            age.value ?: -1,
            serviceCity.value ?: "",
            livingCity.value ?: ""
        ))

        val lesionId = patientsDAO.insert(LesionDataDTO(
            bodyRegion.value ?: "",
            diagnostic.value ?: "",
            diagnosticSecondary.value ?: "",
            woundGrown.value ?: false,
            woundItched.value ?: false,
            woundBleed.value ?: false,
            woundHurt.value ?: false,
            woundPatternChanged.value ?: false,
            woundHasRelief.value ?: false,
            patientDataId.toInt()
        ))

        val list = _imagePathList.value
        list?.forEach {
            patientsDAO.insert(LesionImageDTO(it, lesionId.toInt()))
        }

        clearAllData()
    }

    fun clearAllData(){
        susNumber.value = null
        age.value = null
        serviceCity.value = null
        livingCity.value = null

        bodyRegion.value = null
        diagnostic.value = null
        diagnosticSecondary.value = null
        woundGrown.value = null
        woundItched.value = null
        woundBleed.value = null
        woundHurt.value = null
        woundPatternChanged.value = null
        woundHasRelief.value = null

        _imagePathList.value = null
        currentImagePath.value = null
    }

}