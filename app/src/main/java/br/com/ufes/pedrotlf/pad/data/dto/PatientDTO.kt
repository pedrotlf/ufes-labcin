package br.com.ufes.pedrotlf.pad.data.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PatientDTO(
    @Embedded
    val patientData: PatientDataDTO,

    @Relation(parentColumn = "id", entityColumn = "patientId", entity = LesionDataDTO::class)
    val lesions: List<LesionDTO>
): Parcelable

@Parcelize
@Entity(tableName = "table_patient")
data class PatientDataDTO(
    val susNumber: String,
    val age: Int,
    val serviceCity: String,
    val livingCity: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable