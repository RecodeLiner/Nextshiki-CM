package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.generateImageLoader
import com.rcl.nextshiki.models.searchobject.ObjById
import com.rcl.nextshiki.widthSizeClass
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter

object ContentObject {
    @Composable
    fun ContentObject(value: ObjById) {
        widthSizeClass
        when (widthSizeClass.value) {
            Compact ->
                mobileUI(value)

            else ->
                desktopUI(value)
        }
    }

    @Composable
    private fun mobileUI(value: ObjById) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                imageBlock(value)
                textBlock(value)
                scoreBlock(value)
            }
        }
    }

    @Composable
    private fun desktopUI(value: ObjById) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth().weight(1f)) {
                imageBlock(value)
                textBlock(value)
                scoreBlock(value)
            }
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                item {
                    Card(modifier = Modifier.fillMaxSize()) {

                    }
                }
            }
        }
    }

    @Composable
    private fun imageBlock(value: ObjById) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
                val painter = rememberImagePainter(BuildConfig.DOMAIN + value.image!!.original!!)
                Image(
                    modifier = Modifier.width(maxWidth / 2)
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.Center),
                    painter = painter,
                    contentDescription = "Profile image"
                )
            }
        }
    }

    @Composable
    private fun textBlock(value: ObjById) {
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                text = when (Locale.current.language) {
                    "ru" ->
                        value.russian.toString()

                    else ->
                        value.english.toString()
                },
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun scoreBlock(value: ObjById) {
        Box(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = value.score!!,
                )
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Star icon in content"
                )
            }
        }
    }
}