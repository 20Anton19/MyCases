package com.example.mycases.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "weapon_table")
data class WeaponData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val damage: Int
)

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