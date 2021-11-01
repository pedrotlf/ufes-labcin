package br.com.ufes.pedrotlf.pad.data.dto

data class SurgeryPatientLesionDTO(
    val diaMaior: String,
    val diaMenor: String,
    val diagnosis: String,
    val secDiagnosis: String,
    val procedure: String,
    val obs: String,
    val surgeon: String,
    val images: List<String>
)