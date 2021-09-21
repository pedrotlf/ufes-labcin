package br.com.ufes.pedrotlf.pad.data.dto

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LesionDTO(
    @Embedded
    val lessionData: LesionDataDTO,

    @Relation(parentColumn = "id", entityColumn = "lesionId", entity = LesionImageDTO::class)
    val images: List<LesionImageDTO>
): Parcelable

@Parcelize
@Entity(
    tableName = "table_lesion",
    foreignKeys = [
        ForeignKey(
            entity = PatientDataDTO::class,
            parentColumns = ["id"],
            childColumns = ["patientId"]
        )]
)
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
@Entity(
    tableName = "table_lesion_image",
    foreignKeys = [
        ForeignKey(
            entity = LesionDataDTO::class,
            parentColumns = ["id"],
            childColumns = ["lesionId"]
        )]
)
data class LesionImageDTO(
    val image: String,
    val lesionId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable