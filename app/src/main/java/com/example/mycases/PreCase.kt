package com.example.mycases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PreCase(
    weaponViewModel: WeaponViewModel = hiltViewModel(),
    onClick: () -> Unit,
    onClickShowInside: () -> Unit,
    onClickGoMainMenu: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .width(400.dp)
                .height(150.dp),
            onClick = {
                onClick()
            }
        ) {
            Text(
                text = "Открыть",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 50.sp,
                color = Color.White
            )
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