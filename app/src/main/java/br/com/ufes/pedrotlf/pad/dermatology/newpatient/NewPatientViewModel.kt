package br.com.ufes.pedrotlf.pad.dermatology.newpatient

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPatientViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val susNumber = MutableLiveData<String>()
    val age = MutableLiveData<Int>()
    val serviceCity = MutableLiveData<String>()
    val livingCity = MutableLiveData<String>()

    val bodyRegion = MutableLiveData<String>()
    val diagnostic = MutableLiveData<String>()
    val diagnosticSecondary = MutableLiveData<String>()
    val woundGrown = MutableLiveData<Boolean>()
    val woundItched = MutableLiveData<Boolean>()
    val woundBleed = MutableLiveData<Boolean>()
    val woundHurt = MutableLiveData<Boolean>()
    val woundPatternChanged = MutableLiveData<Boolean>()
    val woundHasRelief = MutableLiveData<Boolean>()

    val photosList = MutableLiveData<List<Bitmap>>()
}