package com.example.mycases

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainMenu(
    //weaponViewModel: WeaponViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    onClickGoPreCase: () -> Unit,
    onClickGoMyInventory: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0f at 0 using LinearEasing
                8f at 1000 using LinearEasing
                0f at 2000 using LinearEasing
                -8f at 3000 using LinearEasing
                0f at 4000 using LinearEasing
            },
            repeatMode = RepeatMode.Restart,
        ), label = "поворот кейса"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "gaz") },
                    modifier = Modifier
                        .height(35.dp),
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Blue.copy(alpha = 0.35f)
                    ),

                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                )
                Text(text = "гз")
            },
            content = {
                Image(
                    painter = painterResource(id = R.drawable.bg_main_01),
                    contentDescription = "mainbg",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp, 65.dp, 30.dp, 65.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ){
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .height(100.dp)
                                .clickable {
                                    onClickGoPreCase()
                                }
                                .graphicsLayer(rotationZ = angle)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kalash),
                                contentDescription = "case1",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .height(100.dp)
                                .clickable {
                                    onClickGoPreCase()
                                }
                                .graphicsLayer(rotationZ = angle)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kalash),
                                contentDescription = "case1",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ){
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .height(100.dp)
                                .clickable {
                                    onClickGoPreCase()
                                }
                                .graphicsLayer(rotationZ = angle)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kalash),
                                contentDescription = "case1",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .height(100.dp)
                                .clickable {
                                    onClickGoMyInventory()
                                }
                                .graphicsLayer(rotationZ = angle)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kalash),
                                contentDescription = "case1",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }
                }
            },
            bottomBar = {
                BottomAppBar (
                    modifier = Modifier
                        .height(35.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {  }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        )
    }
}