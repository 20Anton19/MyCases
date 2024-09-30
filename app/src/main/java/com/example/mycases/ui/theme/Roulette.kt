package com.example.mycases.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycases.R


@Composable
fun Roulette(onClick2: () -> Unit) {
    var angle by remember { mutableStateOf(0f) }

    val animatedAngle by animateFloatAsState (
        targetValue = angle,
        animationSpec = tween(
            durationMillis = 5000, // Длительность анимации
            easing = FastOutSlowInEasing // Функция замедления
        ), label = ""
    )


    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier.fillMaxHeight(),
            text = "Газ"
        )
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .width(275.dp)
                    .rotate(animatedAngle),
                painter = painterResource(id = R.drawable.wheel2),
                contentDescription = "Колесо"
            )
            Image(
                modifier = Modifier
                    .width(600.dp),
                painter = painterResource(id = R.drawable.wheel1),
                contentDescription = "Обод"
            )
        }
        Button(
            modifier = Modifier.fillMaxHeight(),
            onClick = {
                angle += (720..1080).random().toFloat()
            }
        ) {
            Text(
                text = "Газ"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    Roulette(onClick2 = {})
}
