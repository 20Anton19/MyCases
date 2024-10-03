package com.example.mycases

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.example.mycases.data.WeaponData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val WeaponDataType = object : NavType<WeaponData>(false) {
        override fun get(bundle: Bundle, key: String): WeaponData? {
            return if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, WeaponData::class.java)
            } else {
                bundle.getParcelable(key)
            }
            //return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): WeaponData {
            // return Json.decodeFromString(Uri.encode(value))
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: WeaponData): String {
            // return Uri.encode(Json.encodeToString(value))
            return Json.encodeToString(value)
        }

        override fun put(bundle: Bundle, key: String, value: WeaponData) {
            // bundle.putString(key, Json.encodeToString(value))
            bundle.putParcelable(key,value)
        }
    }
}