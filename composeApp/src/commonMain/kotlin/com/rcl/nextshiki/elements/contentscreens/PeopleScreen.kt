package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PeopleScreen(data: PeopleObject) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            mobile(data)
        }

        else -> {
            desktop(data)
        }
    }
}

@Composable
private fun mobile(data: PeopleObject) {
    LazyColumn {
        item {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(BuildConfig.DOMAIN + (data.image?.original))
                    .size(Size.ORIGINAL)
                    .build()
            )
            when (painter.state) {
                is AsyncImagePainter.State.Success -> {
                    AsyncPicture(painter)
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Error -> {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = "Error People Screen Icon")
                }

                else -> {

                }
            }
        }
        item {
            CommonName(data.russian, persistentListOf(data.name))
        }
    }
}

@Composable
private fun desktop(data: PeopleObject) {
    Row(modifier = Modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            Box {
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(BuildConfig.DOMAIN + (data.image?.original))
                        .size(Size.ORIGINAL)
                        .build()
                )
                when (painter.state) {
                    is AsyncImagePainter.State.Success -> {
                        AsyncPicture(painter)
                    }

                    is AsyncImagePainter.State.Error -> {
                        Icon(imageVector = Icons.Filled.Error, contentDescription = "Error People Screen Icon")
                    }

                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }

                    else -> {

                    }
                }
            }
            CommonName(data.russian, persistentListOf(data.name))
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
        }
    }
}