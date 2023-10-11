package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.generateImageLoader
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter

object SearchCardObject {
    @Composable
    fun SearchCard(
        modifier: Modifier = Modifier,
        content: SearchListItem
    ){
        Card(
            modifier = Modifier.fillMaxSize().then(modifier)
        ){
            Column {
                CompositionLocalProvider(
                    LocalImageLoader provides remember { generateImageLoader() },
                ) {
                    val painter = rememberImagePainter(BuildConfig.DOMAIN+content.image!!.original!!)
                    Image(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(15.dp)),
                        painter = painter,
                        contentDescription = "Calendar preview image",
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = when (Locale.current.language) {
                        "ru" -> content.russian!!
                        else -> content.name!!
                    },
                    softWrap = true,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}