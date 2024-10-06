package com.example.mycases

import com.example.mycases.data.WeaponData
import kotlinx.serialization.Serializable

sealed class AppScreen {
    @Serializable
    object MainActivityScreen : AppScreen()

    @Serializable
    object CaseOpeningScreen : AppScreen()

    @Serializable
    data class CaseResultScreen(val resultWeapon: WeaponData) : AppScreen()

    @Serializable
    object MyInventoryScreen : AppScreen()

    @Serializable
    object PreCaseScreen : AppScreen()

    @Serializable
    object InsideTheCaseScreen : AppScreen()
}