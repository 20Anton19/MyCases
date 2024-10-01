package com.example.mycases

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
fun MyInventory(weaponViewModel: WeaponViewModel, onClick: () -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    val weaponListWithDate = remember { mutableStateListOf<Pair<WeaponData, Long>>() }
    val originalWeaponList = remember { mutableStateListOf<Pair<WeaponData, Long>>() }
    val inventoryList = weaponViewModel.getInventoryVM().observeAsState(emptyList()).value
    var rowsAmount by remember { mutableStateOf(0) }
    var lastRowLen by remember { mutableStateOf(0) }

    LaunchedEffect(inventoryList) {
        if (inventoryList.isNotEmpty()) {
            repeat(inventoryList.size) { index ->
                val weapon = withContext(Dispatchers.IO) {
                    weaponViewModel.getWeaponVM(inventoryList[index].weaponId)
                }
                weaponListWithDate.add(Pair(weapon, inventoryList[index].dateAdded))
                originalWeaponList.add(Pair(weapon, inventoryList[index].dateAdded))
            }
            rowsAmount = kotlin.math.floor(inventoryList.size / 4.0).toInt()
            lastRowLen = (inventoryList.size % 4.0).toInt()
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.mmm),
                contentDescription = "mainmisha",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        key(weaponListWithDate) {
            rowsAmount = kotlin.math.floor(weaponListWithDate.size / 4.0).toInt()
            lastRowLen = (weaponListWithDate.size % 4.0).toInt()
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
                            if (actualIndex < weaponListWithDate.size) {
                                Column {
                                    val resourceId = ResourceGenerator.getResourceId(
                                        context,
                                        weaponListWithDate[actualIndex].first.name,
                                        weaponListWithDate[actualIndex].first.skin
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
                                        text = weaponListWithDate[actualIndex].first.name
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        Column {
            Button(
                modifier = Modifier.height(30.dp),
                onClick = {
                    val sortedByDate = originalWeaponList.sortedByDescending { it.second }
                    weaponListWithDate.clear()
                    weaponListWithDate.addAll(sortedByDate)
                }
            ) {
                Text(text = "По дате")
            }

            Button(
                modifier = Modifier.height(30.dp),
                onClick = {
                    val sortedByRarity = sortByRarity(originalWeaponList)
                    weaponListWithDate.clear()
                    weaponListWithDate.addAll(sortedByRarity)
                }
            ) {
                Text(text = "По редкости")
            }

            Button(
                modifier = Modifier.height(30.dp),
                onClick = {
                    val sortedByABC = originalWeaponList.sortedBy { it.first.name }
                    weaponListWithDate.clear()
                    weaponListWithDate.addAll(sortedByABC)
                }
            ) {
                Text(text = "По алфавиту")
            }

            Button(
                modifier = Modifier.height(30.dp),
                onClick = {
                    val filteredWeaponList = originalWeaponList.filter { it.first.rarity == "Restricted" }
                    weaponListWithDate.clear()
                    weaponListWithDate.addAll(filteredWeaponList)
                }
            ) {
                Text(text = "Фильтр по Restricted")
            }
        }
    }
}
