package br.com.ufes.pedrotlf.pad.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.ufes.pedrotlf.pad.data.dto.LesionDataDTO
import br.com.ufes.pedrotlf.pad.data.dto.LesionImageDTO
import br.com.ufes.pedrotlf.pad.data.dto.PatientDataDTO
import br.com.ufes.pedrotlf.pad.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [PatientDataDTO::class, LesionDataDTO::class, LesionImageDTO::class], version = 3)
abstract class PatientsDatabase : RoomDatabase(){

    abstract fun patientDAO(): PatientsDAO

    class Callback @Inject constructor(
        private val database: Provider<PatientsDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback()
}