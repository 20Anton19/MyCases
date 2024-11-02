package com.example.mycases

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InsideTheCase(
    caseName: String,
    weaponViewModel: WeaponViewModel = hiltViewModel(),
    insideTheCaseViewModel: InsideTheCaseViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    //onClick: () -> Unit
) {
    val context = LocalContext.current
    var rowsAmount by remember { mutableStateOf(0) }
    var lastRowLen by remember { mutableStateOf(0) }



    val weaponList: MutableList<WeaponData> = mutableListOf()
    LaunchedEffect(Unit) {
        weaponList.clear()
        var tempList = insideTheCaseViewModel.getWeaponListWithoutKnifes()
        if ((tempList.isEmpty()) or (insideTheCaseViewModel.getCaseName() != caseName)) {
            insideTheCaseViewModel.setCaseName(caseName)
            insideTheCaseViewModel.makeAllWork(weaponViewModel)
            tempList = insideTheCaseViewModel.getWeaponListWithoutKnifes()
        }
        weaponList.addAll(tempList)
        rowsAmount = kotlin.math.floor(weaponList.size / 4.0).toInt()
        lastRowLen = (weaponList.size % 4.0).toInt()
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
                    if (actualIndex < weaponList.size) {
                        Column {
                            val resourceId = ResourceGenerator.getResourceId(
                                context,
                                weaponList[actualIndex].name,
                                weaponList[actualIndex].skin
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
                                text = weaponList[actualIndex].name
                            )
                        }
                    }
                }
            }
        }
    }
}



