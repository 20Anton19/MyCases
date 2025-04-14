package com.example.mycases.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "weapon_table")
data class WeaponData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val skin: String,
    val weaponCase: String,
    val rarity: String,
    val quality: String,
    val qualityRu: String,
    val positionInCase: Int,
    val price: Double,
    val statTrack: Boolean
): Parcelable

@Entity(
    tableName = "inventory",
    foreignKeys = [ForeignKey(
        entity = WeaponData::class,
        parentColumns = ["id"],
        childColumns = ["weaponId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["weaponId"])]
)
data class Inventory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val weaponId: Int,
    val dateAdded: Long = System.currentTimeMillis()  // Используем текущую дату и время
)