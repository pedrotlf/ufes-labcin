package br.com.ufes.pedrotlf.pad.data

import br.com.ufes.pedrotlf.pad.BuildConfig
import br.com.ufes.pedrotlf.pad.api.SadeApi
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDataDTO
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientDTO
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientLesionDTO
import br.com.ufes.pedrotlf.pad.generateMultipartList
import okhttp3.ResponseBody

class SadeRepository constructor(private val sadeApi: SadeApi) {

    suspend fun sendDermatologyPatientLesion(
        patient: PatientDataDTO,
        lesionDTO: LesionDTO,
    ): ResponseBody {
        val lesionData = lesionDTO.lessionData
        val lesionImages = lesionDTO.images.map{ it.image }

        return sadeApi.sendDermatologyPatientLesion(patient.susNumber, BuildConfig.REPO_API_TOKEN,
            patient.age, patient.serviceCity, patient.livingCity,
            lesionData.bodyRegion, lesionData.diagnostic, lesionData.diagnosticSecondary,
            lesionData.woundGrown, lesionData.woundItched, lesionData.woundBleed,
            lesionData.woundHasRelief, lesionData.woundHurt, lesionData.woundPatternChanged,
            lesionImages.generateMultipartList("imagem")
        )
    }

    suspend fun getSurgeryPatient(susNumber: String): SurgeryPatientDTO {
        return sadeApi.getSurgeryPatient(susNumber, BuildConfig.REPO_API_TOKEN)
    }

    suspend fun sendSurgeryPatientLesion(
        susNumber: String,
        lesion: SurgeryPatientLesionDTO
    ){
        return sadeApi.sendSurgeryPatientLesion(susNumber, BuildConfig.REPO_API_TOKEN,
            lesion.diaMaior, lesion.diaMenor, lesion.diagnosis, lesion.secDiagnosis,
            lesion.procedure, lesion.obs, lesion.surgeon, 
            lesion.images.generateMultipartList("imagem")
        )
    }

    suspend fun testConnection(baseUrl: String): ResponseBody{
        return sadeApi.testConnection(baseUrl + SadeApi.TEST_CONNECTION_URL)
    }
}