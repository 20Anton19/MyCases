package com.example.mycases.di

import android.app.Application
import androidx.room.Room
import com.example.mycases.data.WeaponDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideWeaponDatabase(app: Application): WeaponDatabase{
        return Room.databaseBuilder(
            app,
            WeaponDatabase::class.java,
            "weapon_database"
        )
        .createFromAsset("wb5.db")
        .build()
    }
}