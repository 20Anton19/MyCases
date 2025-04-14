package com.example.mycases

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun sortByRarity(weaponList: List<Pair<WeaponData, Long>>): List<Pair<WeaponData, Long>> {
    val rarityOrder = mapOf(
        "Contraband" to 5,
        "Covert" to 4,
        "Classified" to 3,
        "Restricted" to 2,
        "Mil-Spec" to 1
    )
    return weaponList.sortedByDescending { rarityOrder[it.first.rarity] ?: 0 }
}

@Composable
fun MyInventory(
    weaponViewModel: WeaponViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onClickGoMainMenu: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    //val weaponListWithDate = remember { mutableStateListOf<Pair<WeaponData, Long>>() }
    val weaponListWithDate by weaponViewModel.weaponListWithDate.collectAsState()
    val originalWeaponList = remember { mutableStateListOf<Pair<WeaponData, Long>>() }
    val myWeaponList = remember { mutableStateListOf<Pair<WeaponData, Long>>() }
    val inventoryList = weaponViewModel.getInventoryVM().observeAsState(emptyList()).value
    var rowsAmount by remember { mutableStateOf(0) }
    var lastRowLen by remember { mutableStateOf(0) }

    originalWeaponList.clear()
    originalWeaponList.addAll(weaponListWithDate)
    myWeaponList.clear()
    myWeaponList.addAll(weaponListWithDate)
    rowsAmount = kotlin.math.floor(weaponListWithDate.size / 4.0).toInt()
    lastRowLen = (weaponListWithDate.size % 4.0).toInt()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.mmm),
                contentDescription = "mainmisha",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
    else {
        rowsAmount = kotlin.math.floor(weaponListWithDate.size / 4.0).toInt()
        lastRowLen = (weaponListWithDate.size % 4.0).toInt()
        Column {
            LazyRow(
            ) {
                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val sortedByDate = originalWeaponList.sortedByDescending { it.second }
                            myWeaponList.clear()
                            myWeaponList.addAll(sortedByDate)
                        }
                    ) {
                        Text(text = "По дате")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val sortedByRarity = sortByRarity(originalWeaponList)
                            myWeaponList.clear()
                            myWeaponList.addAll(sortedByRarity)
                        }
                    ) {
                        Text(text = "По редкости")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val sortedByABC = originalWeaponList.sortedBy { it.first.name }
                            myWeaponList.clear()
                            myWeaponList.addAll(sortedByABC)
                        }
                    ) {
                        Text(text = "По алфавиту")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Mil-Spec" }
                            myWeaponList.clear()
                            myWeaponList.addAll(filteredWeaponList)
                        }
                    ) {
                        Text(text = "Фильтр по Mil-Spec")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Restricted" }
                            myWeaponList.clear()
                            myWeaponList.addAll(filteredWeaponList)
                        }
                    ) {
                        Text(text = "Фильтр по Restricted")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Classified" }
                            myWeaponList.clear()
                            myWeaponList.addAll(filteredWeaponList)
                        }
                    ) {
                        Text(text = "Фильтр по Classified")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Covert" }
                            myWeaponList.clear()
                            myWeaponList.addAll(filteredWeaponList)
                        }
                    ) {
                        Text(text = "Фильтр по Covert")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Contraband" }
                            myWeaponList.clear()
                            myWeaponList.addAll(filteredWeaponList)
                        }
                    ) {
                        Text(text = "Фильтр по Contraband")
                    }
                }

                item {
                    Button(
                        modifier = Modifier.height(35.dp),
                        onClick = {
                            onClickGoMainMenu()
                        }
                    ) {
                        Text(
                            text = "В меню"
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rowsAmount + if (lastRowLen > 0) 1 else 0) { rowIndex ->
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        val itemCountInRow = if (rowIndex == rowsAmount && lastRowLen > 0) {
                            lastRowLen
                        } else {
                            4
                        }

                        items(itemCountInRow) { index ->
                            val actualIndex = rowIndex * 4 + index
                            if (actualIndex < myWeaponList.size) {
                                Column {
                                    val resourceId = ResourceGenerator.getResourceId(
                                        context,
                                        myWeaponList[actualIndex].first.name,
                                        myWeaponList[actualIndex].first.skin
                                    )
                                    Image(
                                        painter = painterResource(id = resourceId),
                                        contentDescription = "mainbg",
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = myWeaponList[actualIndex].first.name
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
