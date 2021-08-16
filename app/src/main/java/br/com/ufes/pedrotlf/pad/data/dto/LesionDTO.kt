package br.com.ufes.pedrotlf.pad.data.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

data class LesionDTO(
    @Embedded
    val lessionData: LesionDataDTO,

    @Relation(parentColumn = "id", entityColumn = "lesionId", entity = LesionImageDTO::class)
    val images: List<LesionImageDTO>
)

@Parcelize
@Entity(tableName = "table_lesion")
data class LesionDataDTO(
    val bodyRegion: String,
    val diagnostic: String,
    val diagnosticSecondary: String,
    val woundGrown: Boolean,
    val woundItched: Boolean,
    val woundBleed: Boolean,
    val woundHurt: Boolean,
    val woundPatternChanged: Boolean,
    val woundHasRelief: Boolean,
    val patientId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable

@Parcelize
@Entity(tableName = "table_lesion_image")
data class LesionImageDTO(
    val image: String,
    val lesionId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable