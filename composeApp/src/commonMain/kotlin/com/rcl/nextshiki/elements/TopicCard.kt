package com.rcl.nextshiki.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
@Stable
fun TopicCard(
    onClick: () -> Unit,
    title: String,
    backgroundPainter: Painter,
    userPainter: Painter,
    userNickname: String
) {
    Box(modifier = Modifier.fillMaxWidth().aspectRatio(1F).clip(RoundedCornerShape(25.dp)).noRippleClickable(onClick)) {
        Image(
            painter = backgroundPainter,
            contentDescription = "News preview pic",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.9f)
                )
                .padding(10.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = title,
                maxLines = 2,
                color = Color.White
            )
            Row(modifier = Modifier.padding(top = 5.dp)) {
                Image(
                    painter = userPainter,
                    contentDescription = "News user image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(25.dp).width(25.dp).clip(RoundedCornerShape(25.dp))
                )
                Text(
                    text = userNickname,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.White
                )
            }
        }
    }
}