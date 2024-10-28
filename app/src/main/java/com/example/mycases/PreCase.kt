package com.example.mycases

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PreCase(
    weaponViewModel: WeaponViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    caseOpeningViewModel: CaseOpeningViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onClick: () -> Unit,
    onClickShowInside: () -> Unit,
    onClickGoMainMenu: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var isButtonEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (caseOpeningViewModel.preloadImages().isEmpty()) {
            Log.d("EbasoEbaso", "Ты гандон")
            caseOpeningViewModel.makeAllWork(weaponViewModel, context)
        }
        isLoading = false
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            Button(
                modifier = Modifier
                    .width(400.dp)
                    .height(150.dp),
                onClick = {
                }
            ) {
                Text(
                    text = "Открыть",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 50.sp,
                    color = Color.Gray
                )
            }
        }
        else {
            Button(
                modifier = Modifier
                    .width(400.dp)
                    .height(150.dp),
                onClick = {
                    if (isButtonEnabled) {
                        isButtonEnabled = false
                        // Запускаем долгую операцию
                        CoroutineScope(Dispatchers.Main).launch {
                            onClick()
                            delay(100)
                            isButtonEnabled = true
                        }
                    }
                },
                enabled = isButtonEnabled
            ) {
                Text(
                    text = "Открыть",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 50.sp,
                    color = Color.White
                )
            }
        }

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(100.dp),
            onClick = {
                onClickShowInside()
            }
        ) {
            Text(
                text = "Что внутри?",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                color = Color.White
            )
        }
    }
    Button(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp),
        onClick = {
            onClickGoMainMenu()
        }
    ) {
        Text(
            text = "Назад",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            color = Color.White
        )
    }
}