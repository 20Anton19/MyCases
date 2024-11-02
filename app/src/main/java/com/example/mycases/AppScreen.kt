package com.example.mycases

import com.example.mycases.data.WeaponData
import kotlinx.serialization.Serializable

sealed class AppScreen {
    @Serializable
    object MainMenuScreen : AppScreen()

    @Serializable
    object CaseOpeningScreen : AppScreen()

    @Serializable
    data class CaseResultScreen(val resultWeapon: WeaponData) : AppScreen()

    @Serializable
    object MyInventoryScreen : AppScreen()

    @Serializable
    class PreCaseScreen(val caseName: String) : AppScreen()

    @Serializable
    class InsideTheCaseScreen(val caseName: String)  : AppScreen()
}