package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PatientDetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle
) : ViewModel() {

    val patient = stateHandle.getLiveData<PatientDTO>("patient")
}