package br.com.ufes.pedrotlf.pad.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurgeryPatientLesionDTO(
    val region: String,
    val diaMaior: String,
    val diaMenor: String,
    val diagnosis: String,
    val secDiagnosis: String,
    val procedure: String,
    val obs: String,
    val surgeon: String,
): Parcelable