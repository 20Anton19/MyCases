package com.example.mycases

import android.util.Log
import androidx.lifecycle.*
import com.example.mycases.data.Inventory
import com.example.mycases.data.WeaponDao
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.launch

class WeaponViewModel(private val weaponDao: WeaponDao) : ViewModel() {

    val inventory: LiveData<List<Inventory>> = weaponDao.getAllInventory()

    val weaponList: LiveData<List<WeaponData>> = weaponDao.getAllWeapons()

    fun addRandomWeapon() {
        viewModelScope.launch {
            val randomWeapon = weaponDao.getRandomWeapon()
            val newInventoryItem = Inventory(id = 0, weaponId = randomWeapon.id)
            weaponDao.insertOrUpdateInventory(newInventoryItem)
        }
    }

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
    */
    class Factory(private val weaponDao: WeaponDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeaponViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeaponViewModel(weaponDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}