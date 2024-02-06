package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.DOMAIN
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.Size.Companion.ORIGINAL
import com.rcl.nextshiki.models.searchobject.SearchCardModel

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    content: SearchCardModel
) {
    Card(
        modifier = Modifier.fillMaxSize().then(modifier)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(DOMAIN + content.image.original!!)
                    .size(ORIGINAL)
                    .build(),
                contentDescription = "Search card preview image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(15.dp))
            )

            Text(
                text = when (Locale.current.language) {
                    "ru" -> content.russian!!
                    else -> content.english!!
                },
                softWrap = true,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}