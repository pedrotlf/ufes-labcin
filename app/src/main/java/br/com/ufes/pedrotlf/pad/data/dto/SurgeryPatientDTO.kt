package br.com.ufes.pedrotlf.pad.data.dto

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurgeryPatientDTO(
    @JsonProperty("nome") val name: String,
    @JsonProperty("pront") val medicalRecord: String,
    @JsonProperty("alergia") val allergy: String,
    @JsonProperty("diabetes") val diabetes: String,
    @JsonProperty("anticoagulante") val anticoagulant: String,
    @JsonProperty("has") val has: String,
    @JsonProperty("pressao_sis") val systolicPressure: Double,
    @JsonProperty("nLesoes") val numLesions: Int,
    @JsonProperty("pressao_dis") val diastolicPressure: Double,
): Parcelable