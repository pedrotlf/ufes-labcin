package br.com.ufes.pedrotlf.pad

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * ATTENTION! [this] MUST BE A LIST OF FILE PATHS
 *
 * Returns a list of [MultipartBody.Part] using [paramName] as its name. Usually used to send an
 * array of files using the same parameter name.
 */
fun List<String>.generateMultipartList(paramName: String): List<MultipartBody.Part>{
    val multipartList = mutableListOf<MultipartBody.Part>()
    forEach {
        val file = File(it)
        val requestBody = file.asRequestBody("image/*".toMediaType())
        multipartList.add(MultipartBody.Part.createFormData(paramName, file.name, requestBody))
    }

    return multipartList
}