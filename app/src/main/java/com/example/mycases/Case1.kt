package com.example.mycases

import android.graphics.fonts.FontFamily
import android.media.MediaPlayer
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

///////Списки картинок оружия по цветам///////
val milspecList = listOf(
    R.drawable.ssg,
    R.drawable.nova,
    R.drawable.ump45,
    R.drawable.xm1014,
    R.drawable.tec9,
    R.drawable.ssg,
    R.drawable.mac10
)
val restrictedList = listOf(
    R.drawable.sawedoff,
    R.drawable.mp7,
    R.drawable.fiveseven,
    R.drawable.m4a4,
    R.drawable.glock18
)
val classifiedList = listOf(
    R.drawable.zeusx27,
    R.drawable.usps,
    R.drawable.m4a4s,
)
val covertList = listOf(
    R.drawable.awp,
    R.drawable.ak47
)
val exceedingly_rareList = listOf(
    R.drawable.ssg
)

////////Сам алгоритм рандома////////////////////
fun randomWeapon(): Int {
    var weaponImageName: Int = 0
    val choise = (0..9999).random()
    when (choise) {
        in 0..25 -> {
            weaponImageName = exceedingly_rareList.random()
        }
        in 26..89 -> {
            weaponImageName = covertList.random()
        }
        in 90..409 -> {
            weaponImageName = classifiedList.random()
        }
        in 320..2007 -> {
            weaponImageName = restrictedList.random()
        }
        in 1598..9999 -> {
            weaponImageName = milspecList.random()
        }
    }
    return weaponImageName
}
////////////////////////////////////////////////

val images = listOf(
    R.drawable.ak47,
    R.drawable.awp,
    R.drawable.fiveseven,
    R.drawable.glock18,
    R.drawable.m4a4,
    R.drawable.m4a4s,
    R.drawable.mac10,
    R.drawable.mp7,
    R.drawable.nova,
    R.drawable.sawedoff,
    R.drawable.ssg,
    R.drawable.tec9,
    R.drawable.ump45,
    R.drawable.usps,
    R.drawable.xm1014,
    R.drawable.zeusx27,
    R.drawable.ak47,
    R.drawable.awp,
    R.drawable.fiveseven,
    R.drawable.glock18,
    R.drawable.m4a4,
    R.drawable.m4a4s,
    R.drawable.mac10,
    R.drawable.mp7,
    R.drawable.nova,
    R.drawable.sawedoff,
    R.drawable.ssg,
    R.drawable.tec9,
    R.drawable.ump45,
    R.drawable.usps,
    R.drawable.xm1014,
    R.drawable.zeusx27,
    R.drawable.ak47,
    R.drawable.awp,
    R.drawable.fiveseven,
    R.drawable.glock18,
    R.drawable.m4a4,
    R.drawable.m4a4s,
    R.drawable.mac10,
    R.drawable.mp7,
    R.drawable.nova,
    R.drawable.sawedoff,
    R.drawable.ssg,
    R.drawable.tec9,
    R.drawable.ump45,
    R.drawable.usps,
    R.drawable.xm1014,
    R.drawable.zeusx27
)
val weaponList = mutableStateListOf<Int>()
@Composable
fun Case1(onClick: (Any?) -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val context = LocalContext.current
    var lastIndex by remember { mutableStateOf(-1) }


    LaunchedEffect(Unit) {
        val itemSize = 25.dp

        val itemSizePx = with(density) { itemSize.toPx() }
        val itemsScrollCount = (297..303).random()
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

    // LazyRow with animated offset
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
            repeat(47) {
                weaponList.add(randomWeapon())
            }
            items(47) { index ->
                // запасной вариант, но вроде как более лагучий
                //if (index >= weaponList.size) {
                    // Добавляем новый элемент в список, если его еще нет
                    //weaponList.add(randomWeapon())
                //}
                Image(
                    painter = painterResource(id = weaponList[index]),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(150.dp),
                    contentScale = ContentScale.Fit
                )
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


@Composable
fun CaseResult(centerItemIndex: Int?, onClick: () -> Unit) {
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
        Image(
            painter = painterResource(id = centerItemIndex!!),
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
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .height(80.dp)
            ) {
                Text(
                    text = "CONGRATS!!!",
                    fontWeight = FontWeight.ExtraBold,  // Используйте нужный вес шрифта
                    fontSize = 50.sp,               // Используйте нужный размер шрифта
                    color = Color.White
                )
            }

        }
    }
}
