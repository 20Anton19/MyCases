package com.example.mycases

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

/*
@Composable
fun Case1(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color.Green)
            .fillMaxSize()
    ) {

        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = {
                onClick()
            }
        ) {

        }
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = "mainbg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
*/

val images = listOf(
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.awp,
    R.drawable.kalash,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.awp,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.awp,
    R.drawable.kalash,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.awp,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash,
    R.drawable.kalash
)

@Composable
fun Case1(onClick: (Any?) -> Unit) {
    // Sample list of images



    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val context = LocalContext.current
    var lastIndex by remember { mutableStateOf(-1) }


    LaunchedEffect(Unit) {
        val itemSize = 50.dp

        val itemSizePx = with(density) { itemSize.toPx() }
        val itemsScrollCount = 150
        coroutineScope.launch {
            listState.animateScrollBy(
                value = itemSizePx * itemsScrollCount,
                animationSpec = tween(durationMillis = 12000, easing = LinearOutSlowInEasing)
            )
            // After the animation ends, calculate the centered item
            val firstVisibleItem = listState.firstVisibleItemIndex
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset

            // Calculate the center position
            val center = listState.layoutInfo.viewportEndOffset / 2

            // Find the item closest to the center
            val centerItemIndex = listState.layoutInfo.visibleItemsInfo.minByOrNull {
                Math.abs(it.offset + it.size / 2 - center)
            }?.index


            delay(300) // задержка
            onClick(centerItemIndex)
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
            items(images.size) { index ->
                Image(
                    painter = painterResource(id = images[index]),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(150.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Box( //пустой контейнер - витрина, чтобы не давать трогать lazyrow
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {}
        )
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
            painter = painterResource(id = images[centerItemIndex!!]),
            contentDescription = null,
            modifier = Modifier
                .size((100 * animatedScale).dp), // Применяем масштаб к размеру изображения
            contentScale = ContentScale.Fit
        )
    }
}
