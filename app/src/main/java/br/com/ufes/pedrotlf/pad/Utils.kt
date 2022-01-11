package br.com.ufes.pedrotlf.pad

import android.content.Context
import android.os.Environment
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * ATTENTION! [this] MUST BE A LIST OF FILE PATHS
 *
 * Returns a list of [MultipartBody.Part] using [paramName] as its name. Usually used to send an
 * array of files in a request, using the same parameter name.
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

fun <T> deserializeFromJson(jsonString: String): T {
    val mapper = ObjectMapper()
    return mapper.readValue(jsonString, object: TypeReference<T>(){})
}

fun Context.loadJSONFromAssets(path: String): String = assets.open(path).use {
    it.readBytes().toString(Charsets.UTF_8)
}

fun AutoCompleteTextView.setAutoCompleteOptions(content: List<String>){
    setAdapter(
        ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            content
        )
    )
    onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (hasFocus && text.isEmpty())
            showDropDown()
    }

//    setOnTouchListener { _, event ->
//        if(event.action == MotionEvent.ACTION_DOWN) showDropDown()
//        performClick()
//        onTouchEvent(event)
//    }
}

fun Context.getDiagnosisList() = deserializeFromJson<List<String>>(loadJSONFromAssets("listaAutoCompDiag.json"))
fun Context.getCitiesList() = deserializeFromJson<List<String>>(loadJSONFromAssets("listaAutoCompMunicipios.json"))
fun Context.getProceduresList() = deserializeFromJson<List<String>>(loadJSONFromAssets("listaAutoCompProc.json"))
fun Context.getRegionList() = deserializeFromJson<List<String>>(loadJSONFromAssets("listaAutoCompRegiao.json"))

@Throws(IOException::class)
fun Context.createImageFile(): File? {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("pt-BR")).format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

