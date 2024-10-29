package com.example.mycases.data

import androidx.core.location.LocationRequestCompat.Quality
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponDao {
    //@Query("SELECT * FROM weapon_table ORDER BY RANDOM() LIMIT 1")
    //suspend fun getRandomWeapon(): WeaponData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeapon(weapon: WeaponData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateInventory(inventory: Inventory)

    @Update
    suspend fun updateInventory(inventory: Inventory)


    @Query("SELECT * FROM weapon_table")
    fun getAllWeapons(): LiveData<List<WeaponData>>

    @Query("SELECT * FROM inventory")
    fun getAllInventory(): LiveData<List<Inventory>>

    @Query("SELECT * FROM inventory")
    fun getAllInventoryFlow(): Flow<List<Inventory>>

    @Query("SELECT * FROM inventory ORDER BY dateAdded DESC")
    fun getAllInventorySortedByDate(): LiveData<List<Inventory>>

    // Получение данных об оружии по его id
    @Query("SELECT * FROM weapon_table WHERE id = :weaponId")
    suspend fun getWeaponById(weaponId: Int): WeaponData


    //Получаем рандомно разные качества

    @Query("SELECT * FROM weapon_table WHERE rarity = :rarityParam AND quality = :qualityParam ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWeapon(rarityParam: String, qualityParam: String): WeaponData

    //
    @Query("SELECT * FROM weapon_table WHERE weaponCase = :caseName AND quality = 'Factory New' ORDER BY positionInCase")
    suspend fun getWeaponsInsideTheCase(caseName: String): List<WeaponData>

    /*
        @Query("DELETE FROM weapon_table")
        suspend fun clearWeaponData()

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertWeapon(weapon: WeaponData)

        @Query("SELECT * FROM weapon_table LIMIT 1")
        fun getWeapon(): LiveData<WeaponData?>

        @Query("SELECT * FROM weapon_table")
        fun getAllWeapons(): LiveData<List<WeaponData>>*\

     */
}

