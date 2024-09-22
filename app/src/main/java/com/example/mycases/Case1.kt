package com.example.mycases

import android.content.Context
import android.graphics.fonts.FontFamily
import android.media.MediaPlayer
import android.util.Log
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

///////Списки картинок оружия по цветам(редкости)///////
/*
val milspecList = listOf(
    WeaponData(R.drawable.ssg, 3600, "SSG", "Армейское", 0.0f),
    WeaponData(R.drawable.nova, 3600, "Nova", "Армейское", 0.0f),
    WeaponData(R.drawable.ump45, 3600, "Ump-45", "Армейское", 0.0f),
    WeaponData(R.drawable.xm1014, 3600, "XM1014", "Армейское", 0.0f),
    WeaponData(R.drawable.tec9, 3600, "TEC-9", "Армейское", 0.0f),
    WeaponData(R.drawable.ssg, 3600, "SSG", "Армейское", 0.0f),
    WeaponData(R.drawable.mac10, 3600, "Mac-10", "Армейское", 0.0f)
)
val restrictedList = listOf(
    WeaponData(R.drawable.sawedoff, 3600, "Sawedoff", "Запрещённое", 0.0f),
    WeaponData(R.drawable.mp7, 3600, "MP-7", "Запрещённое", 0.0f),
    WeaponData(R.drawable.fiveseven, 3600, "Fiveseven", "Запрещённое", 0.0f),
    WeaponData(R.drawable.m4a4, 3600, "M4A4", "Запрещённое", 0.0f),
    WeaponData(R.drawable.glock18, 3600, "Glock-18", "Запрещённое", 0.0f)
)
val classifiedList = listOf(
    WeaponData(R.drawable.zeusx27, 3600, "Zeusx27", "Засекреченное", 0.0f),
    WeaponData(R.drawable.usps, 3600, "Usp-s", "Засекреченное", 0.0f),
    WeaponData(R.drawable.m4a4s, 3600, "M4A4-s", "Засекреченное", 0.0f)
)
val covertList = listOf(
    WeaponData(R.drawable.awp, 3600, "AWP", "Тайное", 0.0f),
    WeaponData(R.drawable.ak47, 3600, "AK-47", "Тайное", 0.0f)
)
val exceedingly_rareList = listOf(
    WeaponData(R.drawable.ssg, 3600, "Кукри", "Необычайно редкое", 0.0f)
)
*/
private fun randomRarity(): String {
    val choise = (0..9999).random()
    var rarity: String
    when (choise) {
        in 0..25 -> {
            rarity = "contraband"
        }
        in 26..89 -> {
            rarity = "secret"
        }
        in 90..409 -> {
            rarity = "classified"
        }
        in 410..2007 -> {
            rarity = "prohibited"
        }
        in 2008..9999 -> {
            rarity = "army"
        }
        else -> {
            Log.d("WeaponFragment", "Выпала дичь какая-то: choise = $choise")
            rarity = "Ошибка"
        }
    }
    return rarity
}

private fun randomQuality(): String {
    var fvalueInt = (0..10000).random()
    var quality: String
    when (fvalueInt) {
        in 0..700 -> {
            quality = "Factory_New"
        }
        in 701..1500 -> {
            quality = "Minimal_Wear"
        }
        in 1501..3700 -> {
            quality = "Field-Tested"
        }
        in 3701..4400 -> {
            quality = "Well-Worn"
        }
        in 4401 ..10000 -> {
            quality = "Battle-Scarred"
        }
        else -> {
            Log.d("WeaponFragment", "Выпала дичь какая-то")
            quality = "Ошибка"
        }
    }
    return quality
}
////////Сам алгоритм рандома////////////////////
suspend fun randomWeapon(weaponViewModel: WeaponViewModel): WeaponData {
    return suspendCoroutine { continuation ->
        var weapon = WeaponData(0, "Шаблон", "Шаблон", "Шаблон", "Шаблон", "Шаблон", 10.0, true)
        /*
        val choise = (0..9999).random()
        var fvalueInt = (0..10000).random()
        var rarity: String
        var quality: String
        //val fvalue = fvalueInt/10000.0f
        when (choise) {
            in 0..25 -> {
                rarity = "contraband"
            }
            in 26..89 -> {
                rarity = "secret"
            }
            in 90..409 -> {
                rarity = "classified"
            }
            in 410..2007 -> {
                rarity = "prohibited"
            }
            in 2008..9999 -> {
                rarity = "army"
            }
            else -> {
                Log.d("WeaponFragment", "Выпала дичь какая-то: choise = $choise")
                rarity = "Ошибка"
            }
        }
        when (fvalueInt) {
            in 0..700 -> {
                quality = "Factory_New"
            }
            in 701..1500 -> {
                quality = "Minimal_Wear"
            }
            in 1501..3700 -> {
                quality = "Field-Tested"
            }
            in 3701..4400 -> {
                quality = "Well-Worn"
            }
            in 4401 ..10000 -> {
                quality = "Battle-Scarred"
            }
            else -> {
                Log.d("WeaponFragment", "Выпала дичь какая-то: choise = $choise")
                quality = "Ошибка"
            }
        }
        */
        //val rarity = randomRarity()
        //val quality = randomQuality()

        val rarity = "secret"
        val quality = "Battle-Scarred"

        weaponViewModel.getRandomWeaponVM(rarity, quality) { randWeapon ->
            if (randWeapon == null) {
                Log.d("WeaponFragment", "Такого оружия не нашлось")
                continuation.resume(weapon)
            } else {
                Log.d("WeaponFragment", "Оружие: $randWeapon")
                continuation.resume(randWeapon)
            }
        }
    }
}
////////////////////////////////////////////////

val weaponList = mutableStateListOf<WeaponData>()


@Composable
fun Case1(weaponViewModel: WeaponViewModel, onClick: (Any?) -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current


    var lastIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    // Хранение состояния загрузки
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
    } else {
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
                    val context = LocalContext.current
                    val resourceId = context.resources.getIdentifier(weaponList[index].nameImg, "drawable", context.packageName)
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
                        Log.d("MyEx", "resourceId был нулевой")
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


@Composable
fun CaseResult(centerItemIndex: WeaponData?, weaponViewModel: WeaponViewModel, onClick: () -> Unit) {
    // Переменная состояния для масштаба изображения
    var scale by remember { mutableStateOf(1f) }

    // Анимируемый масштаб изображения
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(
            durationMillis = 500, // Длительность анимации
            easing = FastOutSlowInEasing // Функция замедления
        ), label = ""
    )
    // Запускаем анимацию при создании компоузабл функции
    LaunchedEffect(Unit) {
        scale = 3.5f // Целевой масштаб для анимации
    }

    val brush =  Brush.horizontalGradient(
        listOf(Color.Red, Color.Green, Color.Blue),
        startX = 0.0f,
        endX = 300.0f
    )

    Image(
        painter = painterResource(id = R.drawable.bg_result_01),
        contentDescription = "mainbg",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    // LazyRow with animated offset
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val resourceId = context.resources.getIdentifier(centerItemIndex!!.nameImg, "drawable", context.packageName)
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier
                .size((100 * animatedScale).dp), // Применяем масштаб к размеру изображения
            contentScale = ContentScale.Fit
        )
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 65.dp, 30.dp, 65.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = centerItemIndex!!.name,
                fontWeight = FontWeight.ExtraBold,  // Используйте нужный вес шрифта
                fontSize = 50.sp,               // Используйте нужный размер шрифта
                color = Color.White
            )
            Text(
                text = centerItemIndex!!.rarity,
                fontWeight = FontWeight.ExtraBold,  // Используйте нужный вес шрифта
                fontSize = 50.sp,               // Используйте нужный размер шрифта
                color = Color.White
            )

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 50.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .width(300.dp)
                .background(brush)
        )
    }
}

















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