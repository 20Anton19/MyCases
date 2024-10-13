package com.example.mycases

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.Coil
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.mycases.data.Inventory
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CaseOpening(
    weaponViewModel: WeaponViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    caseOpeningViewModel: CaseOpeningViewModel = hiltViewModel(LocalContext.current as ComponentActivity),

    onClick: (WeaponData) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val context = LocalContext.current
    val weaponList = remember { mutableStateListOf<WeaponData>() }
    var lastIndex by remember { mutableStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }
    var preloadedImages by remember { mutableStateOf(listOf<Int>()) }

    LaunchedEffect(Unit) {
        val itemSize = 1.dp
        val itemSizePx = with(density) { itemSize.toPx() }
        val itemsScrollCount = (7430..7570).random()
        listState.animateScrollBy(
            value = itemSizePx * itemsScrollCount,
            animationSpec = tween(durationMillis = 12000, easing = LinearOutSlowInEasing)
        )

        // Calculate the center position
        val center = listState.layoutInfo.viewportEndOffset / 2

        // Find the item closest to the center
        var centerItemIndex = listState.layoutInfo.visibleItemsInfo.minByOrNull {
            Math.abs(it.offset + it.size / 2 - center)
        }?.index

        delay(300) // задержка
        val weaponForInventory = Inventory(0, weaponList[43]!!.id)
        withContext(Dispatchers.IO) {
            weaponViewModel.insertWeaponToInventory(weaponForInventory)
        }
        onClick(weaponList[43])
    }


        //ddfffdfddfdffd
    LaunchedEffect(Unit) {
        weaponList.clear()
        weaponList.addAll(caseOpeningViewModel.getWeaponListOfRandoms())
        preloadedImages = withContext(Dispatchers.IO) {
            //preloadImages(context, weaponList)
            caseOpeningViewModel.preloadImages(context, weaponList)
        }
        isLoading = false  // Завершаем загрузку
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
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_scroll_01),
                contentDescription = "mainbg",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(47) { index ->
                    // запасной вариант, но вроде как более лагучий
                    //if (index >= weaponList.size) {
                    // Добавляем новый элемент в список, если его еще нет
                    //weaponList.add(randomWeapon())
                    //}
                    val weaponImage = preloadedImages[index]
                    if (weaponImage != 0) {
                        Image(
                            //painter = painterResource(id = resourceId),
                            //Coil
                            painter = rememberImagePainter(data = weaponImage),

                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(150.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    else {
                        Text("Image not found", modifier = Modifier.padding(horizontal = 16.dp))
                            //Log.d("MyEx", "resourceId был нулевой + $imgName")
                    }

                }
            }
            Row( //пустой контейнер - витрина, чтобы не давать трогать lazyrow
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {},
                horizontalArrangement = Arrangement.SpaceAround

            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = "mainbg",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

/*
@Composable
fun CaseOpening(weaponViewModel: WeaponViewModel, onClick: (Any?) -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val context = LocalContext.current
    var lastIndex by remember { mutableStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val itemSize = 1.dp
        val itemSizePx = with(density) { itemSize.toPx() }
        val itemsScrollCount = (7430..7570).random()
        coroutineScope.launch {
            listState.animateScrollBy(
                value = itemSizePx * itemsScrollCount,
                animationSpec = tween(durationMillis = 12000, easing = LinearOutSlowInEasing)
            )

            // Calculate the center position
            val center = listState.layoutInfo.viewportEndOffset / 2

            // Find the item closest to the center
            var centerItemIndex = listState.layoutInfo.visibleItemsInfo.minByOrNull {
                Math.abs(it.offset + it.size / 2 - center)
            }?.index

            delay(300) // задержка
            onClick(weaponList[43])
        }

    }

    LaunchedEffect(listState.firstVisibleItemIndex+2) {
        if (listState.firstVisibleItemIndex != lastIndex) {
            lastIndex = listState.firstVisibleItemIndex
            val mediaPlayer = MediaPlayer.create(context, R.raw.case_sound)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { it.release() }
        }
    }

    LaunchedEffect(Unit) {
        repeat(47) {
            // Получение оружия в асинхронной корутине
            val weapon = withContext(Dispatchers.IO) {
                randomWeapon(weaponViewModel)  // Асинхронный вызов
            }
            weaponList.add(weapon)  // Добавляем новое оружие в список
        }
        isLoading = false  // Завершаем загрузку
    }

    if (isLoading) {
        // Показываем индикатор загрузки, пока данные загружаются
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_scroll_01),
                contentDescription = "mainbg",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(47) { index ->
                    // запасной вариант, но вроде как более лагучий
                    //if (index >= weaponList.size) {
                    // Добавляем новый элемент в список, если его еще нет
                    //weaponList.add(randomWeapon())
                    //}
                    val imgName = SanitizeString.sanitizeString(weaponList[index].name + "__" + weaponList[index].skin)
                    val resourceId = context.resources.getIdentifier(imgName, "drawable", context.packageName)
                    if (resourceId != 0) {
                        Image(
                            painter = painterResource(id = resourceId),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(150.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    else {
                        Text("Image not found", modifier = Modifier.padding(horizontal = 16.dp))
                        Log.d("MyEx", "resourceId был нулевой + $imgName")
                    }

                }
            }
            Row( //пустой контейнер - витрина, чтобы не давать трогать lazyrow
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {},
                horizontalArrangement = Arrangement.SpaceAround

            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = "mainbg",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
 */

/*
LaunchedEffect(Unit) {
    withContext(Dispatchers.IO) {
        try {
            Log.d("MyComposable", "Starting getSkinPrice()")
            getSkinPrice(context)
        } catch (e: Exception) {
            Log.e("MyComposable", "Error in getSkinPrice()", e)
        }
    }
}
/*
fun getSkinPrice(): Int? {
    val url = "https://steamcommunity.com/market/listings/730/StatTrak™%20Nova%20%7C%20Dark%20Sigil%20%28Minimal%20Wear%29"
    //val url = "https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=tag_set_community_33&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_Tournament%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Type%5B%5D=any&category_730_Weapon%5B%5D=any&appid=730"
    return try {
        val doc = Jsoup.connect(url).get()
        val element = doc.getElementById("market_commodity_buyrequests")


        //val text = doc.select("span[class=market_page_fullwidth]")
        // Находим элемент span с классом market_commodity_orders_header_promote
        //val priceElement = doc.select("span.market_commodity_orders_header_promote")
        // Получаем текстовое содержимое элемента
        //val price = priceElement.first()?.text()
        // Получение последнего элемента из найденных
        //val initialPriceElement = text.size
        // Извлечение текста из найденного элемента
        //val initialPrice = initialPriceElement?.text()
        //Log.d("PageNameLogger", "Current page: ${doc.title()}")
        Log.d("PageNameLogger", "Current page: ${element!!.text()}")
        Log.d("PageNameLogger", "Current page: ${element}")
        Log.d("PageNameLogger", "Current page: жп")

    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


fun getSkinPrice(context: Context) {
    val apiKey = "6DF65BD9D30668BFEA9961E71835AF83"
    val appID = "730"
    val url = "https://api.steamapis.com/market/items/$appID?api_key=6DF65BD9D30668BFEA9961E71835AF83"

    val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                // Проверяем, содержит ли ответ массив "items"
                if (response.has("items")) {
                    val items = response.getJSONArray("items")
                    if (items.length() > 0) {
                        val item = items.getJSONObject(0)
                        if (item.has("price")) {
                            val price = item.getString("price")
                            Log.d("SkinPrice", "Цена: $price")
                        } else {
                            Log.e("SkinPrice", "Поле 'price' не найдено в ответе.")
                        }
                    } else {
                        Log.e("SkinPrice", "Массив 'items' пустой.")
                    }
                } else {
                    Log.e("SkinPrice", "Ответ не содержит массива 'items'.")
                }
            } catch (e: JSONException) {
                Log.e("SkinPrice", "Ошибка парсинга JSON: ${e.message}")
                e.printStackTrace()
            }
        },
        { error ->
            Log.e("SkinPrice", "Ошибка запроса: ${error.message}")

            // Выводим информацию об ошибке в лог
            error.networkResponse?.let {
                Log.e("SkinPrice", "Код ошибки: ${it.statusCode}")
                Log.e("SkinPrice", "Ответ от сервера: ${String(it.data)}")
            }
            error.printStackTrace()
        }
    )

    requestQueue.add(jsonObjectRequest)
}*/