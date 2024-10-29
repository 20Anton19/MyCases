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
    suspend fun getWhatIsInside(weaponViewModel: WeaponViewModel): List<WeaponData>{
        return getList(weaponViewModel)
    }

    private suspend fun getList(weaponViewModel: WeaponViewModel): List<WeaponData> {
        return suspendCoroutine { continuation ->
            weaponViewModel.getWhatInTheCase("Kilowatt") { myList ->
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