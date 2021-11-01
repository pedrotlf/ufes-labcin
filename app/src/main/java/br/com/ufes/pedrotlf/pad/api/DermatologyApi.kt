package br.com.ufes.pedrotlf.pad.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface DermatologyApi {

    @Multipart
    @Headers("Accept: application/json")
    @POST("sade/api-smartphone/pacienteDermato/novaLesao/{cartaoSus}")
    suspend fun sendDermatologyPatient(
        @Path("cartaoSus") susNumber: String, @Query("token") token: String,
        @Query("idade") age: Int, @Query("localAtendimento") serviceCity: String?,
        @Query("municipioResidencia") livingCity: String,
        @Query("regiao") region: String, @Query("diagnostico") diagnosis: String,
        @Query("diagnosticoSec") secDiagnosis: String,
        @Query("cresceu") grown: Boolean, @Query("cocou") itched: Boolean,
        @Query("sangrou") bled: Boolean, @Query("relevo") relief: Boolean,
        @Query("doeu") hurt: Boolean, @Query("mudou") changed: Boolean,
        @Part imagem: List<MultipartBody.Part>
    ): ResponseBody
}