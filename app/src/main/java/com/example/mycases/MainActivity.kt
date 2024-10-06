package com.example.mycases

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.mycases.data.WeaponData
import com.example.mycases.data.WeaponDatabase
import com.example.mycases.ui.theme.Roulette
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /////////////////////УБИРАЕМ ЧЕРНЫЕ ПОЛОСЫ ПО БОКАМ И СВЕРХУ////////////////////////
        // Настройка для полноэкранного отображения
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Установите прозрачный фон окна
        // Настройка контроллера инсетов
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Устанавливаем использование вырезов (notches)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        ///////////////////////////////////////////////////////////////////////////////


        val database = WeaponDatabase.getDatabase(this)
        val weaponDao = database.weaponDao()

        val weaponViewModel: WeaponViewModel by viewModels {
            WeaponViewModel.Factory(weaponDao)
        }

        weaponDao.getAllInventory().observe(this, Observer { inventoryList ->
            Log.d("WeaponViewModel", "Инвентарь: $inventoryList")
        })

        weaponDao.getAllWeapons().observe(this, Observer { WeaponList ->
            Log.d("WeaponViewModel", "Список оружий: $WeaponList")
        })


        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = AppScreen.MainActivityScreen
            ) {
                composable<AppScreen.MainActivityScreen> {
                    MyApp(navController, weaponViewModel)
                }

                composable<AppScreen.PreCaseScreen> {
                    PreCase(
                        weaponViewModel = weaponViewModel,
                        onClick = {
                            navController.navigate(AppScreen.CaseOpeningScreen)
                        },
                        onClickShowInside = {
                            navController.navigate(AppScreen.InsideTheCaseScreen)
                        },
                        onClickGoMainActivity = {
                            navController.navigate(AppScreen.MainActivityScreen)
                        }
                    )
                }

                composable<AppScreen.InsideTheCaseScreen> {
                    InsideTheCase(weaponViewModel){

                    }
                }

                composable<AppScreen.CaseOpeningScreen> {
                    CaseOpening(weaponViewModel) {
                        Log.d("NavigationGaz", "Navigating to CaseResultScreen with weapon: $it")
                        navController.navigate(AppScreen.CaseResultScreen(resultWeapon = it))
                    }
                }

                composable<AppScreen.CaseResultScreen> (
                    typeMap = mapOf(
                        typeOf<WeaponData>() to CustomNavType.WeaponDataType
                    )
                ) {
                    val resultWeapon = it.arguments?.getParcelable<WeaponData>("resultWeapon")//вообще не так вроде надо, но работает
                    Log.d("NavigationGaz", "Received weapon in CaseResultScreen: $it")
                    CaseResult(
                        centerItemIndex = resultWeapon,
                        weaponViewModel = weaponViewModel,
                        onClickGoMyInventory = {
                            navController.navigate(AppScreen.MyInventoryScreen) // OnClick для перехода в инвентарь
                        },
                        onClickGoPreCase = {
                            navController.navigate(AppScreen.PreCaseScreen)
                        }
                    )
                }

                composable<AppScreen.MyInventoryScreen> {
                    MyInventory(weaponViewModel) {

                    }
                }
            }

        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MyApp(navController: NavController, weaponViewModel: WeaponViewModel) {
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
                                    navController.navigate(AppScreen.PreCaseScreen)
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
                                    navController.navigate(AppScreen.CaseOpeningScreen)
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
                                    navController.navigate(AppScreen.CaseOpeningScreen)
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
                                    navController.navigate(AppScreen.MyInventoryScreen)
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
