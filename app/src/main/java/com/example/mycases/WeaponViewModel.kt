package com.example.mycases

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.location.LocationRequestCompat.Quality
import androidx.lifecycle.*
import com.example.mycases.data.Inventory
import com.example.mycases.data.WeaponDao
import com.example.mycases.data.WeaponData
import com.example.mycases.data.WeaponDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeaponViewModel @Inject constructor(
    //private val weaponDao: WeaponDao
    val weaponDatabase: WeaponDatabase
) : ViewModel() {

    private val _weaponListWithDate = MutableStateFlow<List<Pair<WeaponData, Long>>>(emptyList())
    val weaponListWithDate: StateFlow<List<Pair<WeaponData, Long>>> = _weaponListWithDate.asStateFlow()

    private val inventory: LiveData<List<Inventory>> = weaponDatabase.weaponDao.getAllInventory()

    private val weaponList: LiveData<List<WeaponData>> = weaponDatabase.weaponDao.getAllWeapons()



    init {
        Log.d("Ebaso", "Я перезапустился")
        viewModelScope.launch {
            weaponDatabase.weaponDao.getAllInventoryFlow().collect { inventoryList ->
                val pairs = inventoryList.map { inventoryItem ->
                    val weapon = withContext(Dispatchers.IO) {
                        weaponDatabase.weaponDao.getWeaponById(inventoryItem.weaponId)
                    }
                    Pair(weapon, inventoryItem.dateAdded)
                }
                _weaponListWithDate.value = pairs
            }
        }
    }

    /*
    fun addRandomWeapon() {
        viewModelScope.launch {
            val randomWeapon = weaponDao.getRandomWeapon()
            val newInventoryItem = Inventory(id = 0, weaponId = randomWeapon.id)
            weaponDao.insertOrUpdateInventory(newInventoryItem)
        }
    }
    */

    fun getWhatInTheCase(caseName: String, onResult: (List<WeaponData>?) -> Unit) {
        viewModelScope.launch {
            try {
                val weaponList = weaponDatabase.weaponDao.getWeaponsInsideTheCase(caseName)
                onResult(weaponList)
            } catch (e: Exception) {
                Log.e("WeaponViewModel", "Ошибка получения оружия: ${e.message}")
                onResult(null)
            }
        }
    }

    fun getRandomWeaponVM(rarity: String, quality: String, onResult: (WeaponData?) -> Unit) {
        viewModelScope.launch {
            try {
                val randomWeapon = weaponDatabase.weaponDao.getRandomWeapon(rarity, quality)
                onResult(randomWeapon)
            } catch (e: Exception) {
                Log.e("WeaponViewModel", "Ошибка получения оружия: ${e.message}")
                onResult(null)
            }
        }
    }

    fun getWeaponVM1(id: Int, onResult: (WeaponData?) -> Unit) {
        viewModelScope.launch {
            try {
                val weapon = weaponDatabase.weaponDao.getWeaponById(id)
                onResult(weapon)
            } catch (e: Exception) {
                Log.e("WeaponViewModel", "Ошибка получения оружия: ${e.message}")
                onResult(null)
            }
        }
    }


    suspend fun getWeaponVM(id: Int): WeaponData {
        return weaponDatabase.weaponDao.getWeaponById(id)
    }

    fun getInventoryVM(): LiveData<List<Inventory>> {
        return inventory
    }

    fun insertWeaponToInventory(inventory: Inventory) {
        viewModelScope.launch {
            weaponDatabase.weaponDao.insertOrUpdateInventory(inventory)
        }
    }
/*
    fun checkWeapons() {
        weaponList.observeForever { weaponL ->
            if (weaponL.isNullOrEmpty()) {
                // Действие, если список null или пуст
                Log.d("WeaponViewModel", "Список оружий пуст или null")
            } else {
                // Действие, если список не пустой
                Log.d("WeaponViewModel", "Список оружий: $weaponL")
            }
        }
    }

    fun checkInventory() {
        inventory.observeForever { invent ->
            if (invent.isNullOrEmpty()) {
                // Действие, если список null или пуст
                Log.d("WeaponViewModel", "Список оружий пуст или null")
            } else {
                // Действие, если список не пустой
                Log.d("WeaponViewModel", "Инвентарь: $invent")
            }
        }
    }

    fun getWeaponDetails(weaponId: Int): LiveData<WeaponData> {
        return liveData {
            emit(weaponDao.getWeaponById(weaponId))
        }
    }

 */
/*
    fun addRandomWeapon() {
        viewModelScope.launch {
            val weapon = weaponDao.getRandomWeapon()
            val existingItem = inventory.value?.find { it.weaponData.id == weapon.id }

            if (existingItem != null) {
                weaponDao.updateInventory(Inventory(existingItem.inventoryId, existingItem.weaponData.id, existingItem.quantity + 1))
            } else {
                weaponDao.insertOrUpdateInventory(Inventory(0, weapon.id, 1))
            }
            Log.d("WeaponViewModel", "Список оружий: ${inventory.value}")
            /*
            weaponDao.insertWeapon(WeaponData(0, "газ", 50))
            weaponDao.insertWeapon(WeaponData(0, "газ1", 150))
            weaponDao.insertWeapon(WeaponData(0, "газ2", 250))
            weaponDao.insertWeapon(WeaponData(0, "газ3", 350))
            weaponDao.insertWeapon(WeaponData(0, "газ4", 450))

            val weapons: LiveData<List<WeaponData>> = weaponDao.getAllWeapons()
            weapons.observeForever { weaponList ->
                if (weaponList.isNullOrEmpty()) {
                    // Действие, если список null или пуст
                    Log.d("WeaponViewModel", "Список оружий пуст или null")
                } else {
                    // Действие, если список не пустой
                    Log.d("WeaponViewModel", "Список оружий: $weaponList")
                }
            }

             */
        }
    }

    fun addRandomWeapon() {
        viewModelScope.launch {
            val randomWeapon = weaponDao.getRandomWeapon()

            // Пытаемся найти запись в инвентаре по этому оружию
            val existingInventory = weaponDao.getWeaponsInInventory().value?.firstOrNull {
                it.weaponData.id == randomWeapon.id
            }

            if (existingInventory != null) {
                // Если оружие уже есть в инвентаре, обновляем количество
                val updatedInventory = Inventory(0, randomWeapon.id, existingInventory.quantity + 1)
                    //existingInventory.copy(quantity = existingInventory.quantity + 1)
                weaponDao.updateInventory(updatedInventory)
            } else {
                // Если оружия нет в инвентаре, создаем новую запись
                val newInventory = Inventory(0, weaponId = randomWeapon.id, quantity = 1)
                weaponDao.insertOrUpdateInventory(newInventory)
            }
        }
    }


    val weapon: LiveData<WeaponData?> = weaponDao.getWeapon()

    fun increaseDamage {
        viewModelScope.launch {
            val currentWeapon = weapon.value
            val currentDamage = currentWeapon?.damage ?: 0
            val newDamage = currentDamage + 30
            val updatedWeapon = WeaponData(id = currentWeapon?.id ?: 510, damage = newDamage)
            weaponDao.insertWeapon(updatedWeapon)
            //weaponDao.clearWeaponData()
        }
    }

    val weapons: LiveData<List<WeaponData>> = weaponDao.getAllWeapons()

    // Проверка на null в LiveData
    fun checkWeapons() {
        weapons.observeForever { weaponList ->
            if (weaponList.isNullOrEmpty()) {
                // Действие, если список null или пуст
                Log.d("WeaponViewModel", "Список оружий пуст или null")
            } else {
                // Действие, если список не пустой
                Log.d("WeaponViewModel", "Список оружий: $weaponList")
            }
        }
    }

    class Factory(private val weaponDao: WeaponDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeaponViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeaponViewModel(weaponDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/
}