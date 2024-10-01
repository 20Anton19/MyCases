package com.example.mycases

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycases.data.Inventory
import com.example.mycases.data.WeaponData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CaseResult(centerItemIndex: WeaponData?, weaponViewModel: WeaponViewModel, onClick: () -> Unit) {
    LaunchedEffect(Unit){
        val weapon = Inventory(0, centerItemIndex!!.id)
        withContext(Dispatchers.IO) {
            weaponViewModel.insertWeaponToInventory(weapon)
        }
    }

    val brush =  Brush.horizontalGradient(
        listOf(Color.Red, Color.Green, Color.Blue),
        startX = 0.0f,
        endX = 300.0f
    )

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
        val context = LocalContext.current
        val resourceId = ResourceGenerator.getResourceId(context, centerItemIndex!!.name, centerItemIndex.skin)
        if (resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
                modifier = Modifier
                    .size((100 * animatedScale).dp), // Применяем масштаб к размеру изображения
                contentScale = ContentScale.Fit
            )
        }
        else {
            Text("Image not found", modifier = Modifier.padding(horizontal = 16.dp))
            Log.d("MyEx", "resourceId был нулевой")
        }
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
            Text(
                text = centerItemIndex!!.price.toString(),
                fontWeight = FontWeight.ExtraBold,  // Используйте нужный вес шрифта
                fontSize = 50.sp,               // Используйте нужный размер шрифта
                color = Color.White
            )
            Text(
                text = centerItemIndex!!.qualityRu,
                fontWeight = FontWeight.ExtraBold,  // Используйте нужный вес шрифта
                fontSize = 30.sp,               // Используйте нужный размер шрифта
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
