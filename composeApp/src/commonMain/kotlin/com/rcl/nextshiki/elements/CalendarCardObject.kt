package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.generateImageLoader
import com.rcl.nextshiki.getString
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun CalendarCard(name: String, link: String, time: String) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .height(220.dp),
) {
    val ins = LocalDateTime.parse(time.split(".")[0]).toInstant(TimeZone.currentSystemDefault())
    val isPast = ins.minus(Clock.System.now()).isNegative()
    Box(modifier = Modifier.fillMaxSize()){
        CompositionLocalProvider(
            LocalImageLoader provides remember { generateImageLoader() },
        ) {
            val painter = rememberImagePainter(BuildConfig.DOMAIN+link)
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = "Calendar preview image",
                contentScale = ContentScale.Crop
            )
        }
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(100.dp)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0F to Color.Transparent,
                        1F to Color.Black
                    )
                )
        )
        Text(
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp),
            text = name,
            color = Color.White
        )
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                text = getString(
                    if (isPast){
                        MR.strings.past_calendar
                    }else{
                        MR.strings.future_calendar
                    }
                )
            )
        }
    }
}