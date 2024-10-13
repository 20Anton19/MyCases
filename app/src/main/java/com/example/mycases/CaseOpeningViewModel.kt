package com.example.mycases

import android.content.Context
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import coil.Coil
import coil.request.ImageRequest
import com.example.mycases.data.WeaponData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CaseOpeningViewModel @Inject constructor() : ViewModel() {
    private fun randomRarity(): String {
        val choice = (0..9999).random()
        val rarity: String
        when (choice) {
            in 0..25 -> {
                rarity = "Contraband"
            }
            in 26..89 -> {
                rarity = "Covert"
            }
            in 90..409 -> {
                rarity = "Classified"
            }
            in 410..2007 -> {
                rarity = "Restricted"
            }
            in 2008..9999 -> {
                rarity = "Mil-Spec"
            }
            else -> {
                Log.d("WeaponFragment", "Выпала дичь какая-то: choise = $choice")
                rarity = "Ошибка"
            }
        }
        return rarity
    }

    private fun randomQuality(): String {
        val fValueInt = (0..10000).random()
        val quality: String
        when (fValueInt) {
            in 0..700 -> {
                quality = "Factory New"
            }
            in 701..1500 -> {
                quality = "Minimal Wear"
            }
            in 1501..3700 -> {
                quality = "Field-Tested"
            }
            in 3701..4400 -> {
                quality = "Well-Worn"
            }
            in 4401 ..10000 -> {
                quality = "Battle-Scarred"
            }
            else -> {
                Log.d("WeaponFragment", "Выпала дичь какая-то")
                quality = "Ошибка"
            }
        }
        return quality
    }

    private suspend fun randomWeapon(weaponViewModel: WeaponViewModel): WeaponData {
        return suspendCoroutine { continuation ->
            val weapon = WeaponData(0, "Шаблон", "Шаблон", "Шаблон", "Шаблон", "Шаблон", "Шаблон",10.0, true)
            val rarity = randomRarity()
            val quality = randomQuality()

            //val rarity = "Covert"
            //val quality = "Battle-Scarred"

            weaponViewModel.getRandomWeaponVM(rarity, quality) { randWeapon ->
                if (randWeapon == null) {
                    Log.d("WeaponFragment", "Такого оружия не нашлось + $rarity + $quality")
                    continuation.resume(weapon)
                } else {
                    Log.d("WeaponFragment", "Оружие: $randWeapon")
                    continuation.resume(randWeapon)
                }
            }
        }
    }

    private val weaponList: MutableList<WeaponData> = mutableListOf()
    private val preloadedImages: MutableList<Int> = mutableListOf()


    fun getWeaponListOfRandoms(): List<WeaponData> {
        return weaponList
    }

    fun preloadImages(): List<Int> {
        return preloadedImages
    }

    init {
        Log.d("EbasoEbaso", "Я создался")
    }

    suspend fun makeAllWork(weaponViewModel: WeaponViewModel, context: Context)
    {
        createWeaponListOfRandoms(weaponViewModel)
        createPreloadImages(context)
    }

    private suspend fun createWeaponListOfRandoms(weaponViewModel: WeaponViewModel) {
        weaponList.clear()
        repeat(47) {
            // Получение оружия в асинхронной корутине
            val weapon = withContext(Dispatchers.IO) {
                //randomWeapon(weaponViewModel)  // Асинхронный вызов
                randomWeapon(weaponViewModel)
            }
            weaponList.add(weapon)  // Добавляем новое оружие в список
        }
        Log.d("EbasoEbaso", "Я прогрузил список $weaponList")
    }

    private fun createPreloadImages(context: Context) {
        preloadedImages.clear()
        val prePreloadedImages = weaponList.map { weapon ->
            val resourceId = ResourceGenerator.getResourceId(context, weapon.name, weapon.skin)
            if (resourceId != 0) {
                // Предзагрузка с помощью Coil
                ImageRequest.Builder(context)
                    .data(resourceId)
                    .allowHardware(false)
                    .build()
                    .let { request -> Coil.imageLoader(context).enqueue(request) }
            }
            resourceId
        }
        preloadedImages.addAll(prePreloadedImages)
        Log.d("EbasoEbaso", "Я прогрузил изображения")
    }
}