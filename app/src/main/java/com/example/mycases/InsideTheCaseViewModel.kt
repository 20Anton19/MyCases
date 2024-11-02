package com.example.mycases

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mycases.data.WeaponData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class InsideTheCaseViewModel @Inject constructor() : ViewModel() {
    suspend fun makeAllWork(weaponViewModel: WeaponViewModel){
        val tempList = getList(weaponViewModel)
        weaponListWithoutKnifes.clear()
        listWithKnifes.clear()
        weaponListWithoutKnifes.addAll(tempList.filterNot { it.rarity == "Contraband" })
        listWithKnifes.addAll(tempList.filter { it.rarity == "Contraband" })
    }

    init {
        Log.d("GasInside", "Я создался")
    }


    private lateinit var caseName: String

    fun setCaseName(caseName: String) {
        this.caseName = caseName
    }

    fun getCaseName(): String? {
        return if (::caseName.isInitialized) {
            caseName
        } else {
            null
        }
    }

    private val weaponListWithoutKnifes: MutableList<WeaponData> = mutableListOf()
    private val listWithKnifes: MutableList<WeaponData> = mutableListOf()

    fun getWeaponListWithoutKnifes(): List<WeaponData> {
        return weaponListWithoutKnifes
    }

    fun getListWithKnifes(): List<WeaponData> {
        return listWithKnifes
    }

    private suspend fun getList(weaponViewModel: WeaponViewModel): List<WeaponData> {
        return suspendCoroutine { continuation ->
            weaponViewModel.getWhatInTheCase(caseName) { myList ->
                if (myList == null) {
                    Log.d("WeaponFragment", "Такого оружия не нашлось")
                    continuation.resumeWithException(Exception("Оружие не найдено"))
                } else {
                    Log.d("WeaponFragment", "Оружие: $myList")
                    continuation.resume(myList)
                }
            }
        }
    }
}