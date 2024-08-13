package com.example.mycases

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class WeaponData(
    val image: Int, val coast: Int, val name: String, val rarity: String
) : Parcelable 
