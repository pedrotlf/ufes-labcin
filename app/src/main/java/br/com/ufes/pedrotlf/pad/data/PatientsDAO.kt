package br.com.ufes.pedrotlf.pad.data

import android.util.Log
import androidx.room.*
import br.com.ufes.pedrotlf.pad.data.dto.*
import kotlinx.coroutines.flow.Flow
import java.io.File

@Dao
abstract class PatientsDAO {

    @Query("SELECT * FROM table_patient ORDER BY table_patient.id DESC")
    abstract fun getPatients(): Flow<List<PatientDTO>>

//    @Query("SELECT * FROM table_lesion WHERE id = :lesionId")
//    fun getLesion(lesionId: Int): Flow<LesionDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(patient: PatientDataDTO): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(lesion: LesionDataDTO): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(lesion: LesionImageDTO): Long

    @Update
    abstract suspend fun update(patient: PatientDataDTO)
    @Update
    abstract suspend fun update(lesion: LesionDataDTO)
    @Update
    abstract suspend fun update(lesion: LesionImageDTO)

    @Delete
    abstract suspend fun delete(patient: PatientDataDTO)
    @Delete
    abstract suspend fun delete(lesion: LesionDataDTO)
    @Delete
    abstract suspend fun delete(lesion: LesionImageDTO)

    @Transaction
    open suspend fun delete(patient: PatientDTO) {
        patient.lesions.forEach {
            it.images.forEach { lesionImageDTO ->
                val path = lesionImageDTO.image
                val fileToDelete = File(path)
                if (fileToDelete.delete()) {
                    Log.i("DeletePatient", "File deleted: $path")
                } else {
                    Log.i("DeletePatient", "Couldn't delete file: $path")
                }
                delete(lesionImageDTO)
            }
            delete(it.lessionData)
        }
        delete(patient.patientData)
    }
}