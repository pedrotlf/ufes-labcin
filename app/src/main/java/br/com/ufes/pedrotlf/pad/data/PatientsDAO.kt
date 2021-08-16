package br.com.ufes.pedrotlf.pad.data

import androidx.room.*
import br.com.ufes.pedrotlf.pad.data.dto.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientsDAO {

    @Query("SELECT * FROM table_patient ORDER By id Desc")
    fun getPatients(): Flow<List<PatientDTO>>

//    @Query("SELECT * FROM table_lesion WHERE id = :lesionId")
//    fun getLesion(lesionId: Int): Flow<LesionDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: PatientDataDTO)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesion: LesionDataDTO)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesion: LesionImageDTO)

    @Update
    suspend fun update(patient: PatientDataDTO)
    @Update
    suspend fun update(lesion: LesionDataDTO)
    @Update
    suspend fun update(lesion: LesionImageDTO)

    @Delete
    suspend fun delete(patient: PatientDataDTO)
    @Delete
    suspend fun delete(lesion: LesionDataDTO)
    @Delete
    suspend fun delete(lesion: LesionImageDTO)
}