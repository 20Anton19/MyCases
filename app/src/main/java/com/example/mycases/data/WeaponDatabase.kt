package com.example.mycases.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeaponData::class, Inventory::class], version = 1, exportSchema = false)
abstract class WeaponDatabase : RoomDatabase() {

    abstract fun weaponDao(): WeaponDao

    companion object {
        @Volatile
        private var INSTANCE: WeaponDatabase? = null

        fun getDatabase(context: Context): WeaponDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeaponDatabase::class.java,
                    "weapon_database"
                )
                .createFromAsset("wb5.db")
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}