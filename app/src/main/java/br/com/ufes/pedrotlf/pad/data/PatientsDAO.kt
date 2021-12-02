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
    abstract suspend fun update(patient: PatientDataDTO): Int
    @Update
    abstract suspend fun update(lesion: LesionDataDTO): Int
    @Update
    abstract suspend fun update(lesion: LesionImageDTO): Int

    @Delete
    abstract suspend fun delete(patient: PatientDataDTO): Int
    @Delete
    abstract suspend fun delete(lesion: LesionDataDTO): Int
    @Delete
    abstract suspend fun unsafeDelete(lesion: LesionImageDTO): Int

    @Transaction
    open suspend fun delete(patient: PatientDTO) {
        patient.lesions.forEach {
            delete(it)
        }
        delete(patient.patientData)
    }

    @Transaction
    open suspend fun delete(lesion: LesionDTO): Int {
        lesion.images.forEach { lesionImageDTO ->
            delete(lesionImageDTO)
        }
        return delete(lesion.lessionData)
    }

    @Transaction
    open suspend fun delete(lesion: LesionImageDTO): Int {
        val path = lesion.image

        val changes = unsafeDelete(lesion)
        return if(changes > 0) {
            val fileToDelete = File(path)
            if (fileToDelete.delete()) {
                Log.i("DeletePatient", "File deleted: $path")
            } else {
                Log.i("DeletePatient", "Couldn't delete file: $path")
            }
            changes
        } else 0
    }
}