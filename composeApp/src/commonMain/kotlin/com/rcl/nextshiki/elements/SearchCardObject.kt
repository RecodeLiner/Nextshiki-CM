package com.rcl.nextshiki.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    name: String
) {
    Card(
        modifier = Modifier.fillMaxSize().then(modifier)
    ) {
        Column {
            Image(
                painter = painter,
                contentDescription = "Search card preview image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(15.dp))
            )
            Text(
                text = name,
                softWrap = true,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}