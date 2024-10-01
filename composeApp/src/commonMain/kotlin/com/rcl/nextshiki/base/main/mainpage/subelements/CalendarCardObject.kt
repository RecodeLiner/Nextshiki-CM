package com.rcl.nextshiki.base.main.mainpage.subelements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rcl.mr.SharedRes.images.calendarcardpreview
import com.rcl.mr.SharedRes.strings.future_calendar
import com.rcl.mr.SharedRes.strings.past_calendar
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import dev.icerock.moko.resources.compose.painterResource
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun CalendarCard(
    name: String,
    time: String,
    painter: Painter,
    timeInstance: Instant = Clock.System.now(),
    onClick: () -> Unit
) {
    val eventInstant = Instant.parse(time)
    val duration = eventInstant.minus(timeInstance)

    Box(modifier = Modifier.fillMaxSize().noRippleClickable(onClick)) {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = Crop,
            modifier = Modifier.fillMaxWidth()
        )
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
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
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
                text = if (duration.isNegative()) {
                    past_calendar.getLocalizableString()
                } else {
                    future_calendar.getLocalizableString(duration.inWholeHours.toInt())
                }
            )
        }
    }
}


@Preview
@Composable
fun CalendarCardPreview() {
    val card = CardElement(
        id = 50713,
        name = "Mahouka Koukou no Rettousei 3rd Season",
        imageLink = "https://shikimori.one/system/animes/preview/50713.jpg?1714776400",
        nextEpisodeAt = "2024-05-17T17:30:00.000+03:00"
    )
    val currentTime = Instant.parse("2024-05-17T15:00:00+03:00")
    val image = painterResource(calendarcardpreview)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(250.dp)) {
            CalendarCard(
                name = card.name,
                time = card.nextEpisodeAt,
                painter = image,
                currentTime
            ) {
                Napier.i("ClickedTo ${card.id}")
            }
        }
    }
}
