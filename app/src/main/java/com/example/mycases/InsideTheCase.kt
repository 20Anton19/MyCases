package com.example.mycases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mycases.data.WeaponData

@Composable
fun InsideTheCase(weaponViewModel: WeaponViewModel = hiltViewModel(), onClick: () -> Unit) {
    val weaponList = remember { mutableStateListOf<WeaponData>() }
    LaunchedEffect(Unit) {

    }
}