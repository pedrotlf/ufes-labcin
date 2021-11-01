package br.com.ufes.pedrotlf.pad.data

import br.com.ufes.pedrotlf.pad.api.DermatologyApi
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDataDTO
import br.com.ufes.pedrotlf.pad.generateMultipartList
import okhttp3.ResponseBody

class DermatologyRepository constructor(private val dermatologyApi: DermatologyApi) {

    suspend fun sendDermatologyPatient(
        patient: PatientDataDTO,
        lesionDTO: LesionDTO,
        token: String
    ): ResponseBody {
        val lesionData = lesionDTO.lessionData
        val lesionImages = lesionDTO.images.map{ it.image }

        return dermatologyApi.sendDermatologyPatient(patient.susNumber, token,
            patient.age, patient.serviceCity, patient.livingCity,
            lesionData.bodyRegion, lesionData.diagnostic, lesionData.diagnosticSecondary,
            lesionData.woundGrown, lesionData.woundItched, lesionData.woundBleed,
            lesionData.woundHasRelief, lesionData.woundHurt, lesionData.woundPatternChanged,
            lesionImages.generateMultipartList("imagem")
        )
    }

    suspend fun testConnection(newBaseUrl: String): ResponseBody{
        return dermatologyApi.testConnection(newBaseUrl + DermatologyApi.TEST_CONNECTION_URL)
    }
}