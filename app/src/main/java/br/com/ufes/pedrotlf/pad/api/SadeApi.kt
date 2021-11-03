package br.com.ufes.pedrotlf.pad.api

import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface SadeApi {

    companion object{
        const val TEST_CONNECTION_URL = "sade/api-smartphone/ack"
    }

    @Multipart
    @Headers("Accept: application/json")
    @POST("sade/api-smartphone/pacienteDermato/novaLesao/{cartaoSus}")
    suspend fun sendDermatologyPatientLesion(
        @Path("cartaoSus") susNumber: String, @Query("token") token: String,
        @Query("idade") age: Int, @Query("localAtendimento") serviceCity: String?,
        @Query("municipioResidencia") livingCity: String,
        @Query("regiao") region: String, @Query("diagnostico") diagnosis: String,
        @Query("diagnosticoSec") secDiagnosis: String,
        @Query("cresceu") grown: Boolean, @Query("cocou") itched: Boolean,
        @Query("sangrou") bled: Boolean, @Query("relevo") relief: Boolean,
        @Query("doeu") hurt: Boolean, @Query("mudou") changed: Boolean,
        @Part images: List<MultipartBody.Part>
    ): ResponseBody

    @GET("sade/api-smartphone/pacienteCirurgia/{cartaoSus}")
    suspend fun getSurgeryPatient(
        @Path("cartaoSus") susNumber: String, @Query("token") token: String
    ): SurgeryPatientDTO

    @Multipart
    @POST("sade/api-smartphone/pacienteCirurgia/novaLesao/{cartaoSus}")
    suspend fun sendSurgeryPatientLesion(
        @Path("cartaoSus") susNumber: String, @Query("token") token: String,
        @Query("regiao") region: String, @Query("diaMaior") diaMaior: String,
        @Query("diaMenor") diaMenor: String, @Query("diagnostico") diagnosis: String,
        @Query("diagnosticoSec") secDiagnosis: String,
        @Query("procedimento") procedure: String, @Query("obs") obs: String,
        @Query("cirurgiao") surgeon: String, @Part images: List<MultipartBody.Part>,
    ): ResponseBody

    @GET("sade/api-smartphone/cirurgioes")
    suspend fun getSurgeons(@Query("token") token: String): List<String>

    @GET
    suspend fun testConnection(@Url url: String): ResponseBody
}