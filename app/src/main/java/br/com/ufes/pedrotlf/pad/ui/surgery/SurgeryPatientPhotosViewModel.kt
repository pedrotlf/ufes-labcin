package br.com.ufes.pedrotlf.pad.ui.surgery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.SadeRepository
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientLesionDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SurgeryPatientPhotosViewModel @Inject constructor(
    private val sadeRepository: SadeRepository
): ViewModel() {

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

    private val _sendPatientLesionRequest = MutableLiveData<Resource<ResponseBody>>()
    val sendPatientLesionRequest: LiveData<Resource<ResponseBody>> = _sendPatientLesionRequest

    fun sendPatientLesion(susNumber: String, patientLesion: SurgeryPatientLesionDTO) = viewModelScope.launch{
        _sendPatientLesionRequest.value = Resource.Loading
        val pathList = imagePathList.value ?: emptyList()
        runCatching {
            sadeRepository.sendSurgeryPatientLesion(susNumber, patientLesion, pathList)
        }.onSuccess {
            deleteFiles()
            _sendPatientLesionRequest.value = Resource.Success(it)
        }.onFailure {
            _sendPatientLesionRequest.value = Resource.Failure(it)
        }
    }

    private fun deleteFiles(){
        _imagePathList.value?.forEach { path ->
            val fileToDelete = File(path)
            if (fileToDelete.delete()) {
                Log.i("DeletePatient", "File deleted: $path")
            } else {
                Log.i("DeletePatient", "Couldn't delete file: $path")
            }
        }
    }
}