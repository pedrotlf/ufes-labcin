package br.com.ufes.pedrotlf.pad.data.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SurgeryPatientDTO(
    @JsonProperty("nome") val name: String,
    @JsonProperty("pront") val medicalRecord: String,
    @JsonProperty("alergia") val allergy: String,
    @JsonProperty("anticoagulante") val anticoagulant: String,
    @JsonProperty("has") val has: String,
    @JsonProperty("pressao_sis") val systolicPressure: Double,
    @JsonProperty("nLesoes") val numLesions: Int,
    @JsonProperty("pressao_dis") val diastolicPressure: Double,
)