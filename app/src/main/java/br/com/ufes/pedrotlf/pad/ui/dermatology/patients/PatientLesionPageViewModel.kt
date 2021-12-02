package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.lifecycle.ViewModel
import br.com.ufes.pedrotlf.pad.data.PatientsDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PatientLesionPageViewModel @Inject constructor(
    private val patientsDAO: PatientsDAO
) : ViewModel() {

    var bodyRegion = ""
    var diagnostic = ""
    var diagnosticSecondary = ""
    var woundGrown: Boolean? = null
    var woundItched: Boolean? = null
    var woundBleed: Boolean? = null
    var woundHurt: Boolean? = null
    var woundPatternChanged: Boolean? = null
    var woundHasRelief: Boolean? = null
}