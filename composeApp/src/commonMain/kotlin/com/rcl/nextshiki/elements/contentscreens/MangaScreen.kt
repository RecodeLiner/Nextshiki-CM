package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.models.searchobject.manga.MangaObject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MangaScreen(data: MangaObject, navigateTo: (String, SearchType) -> Unit) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            mobile(data, navigateTo)
        }

        else -> {
            desktop(data, navigateTo)
        }
    }
}

@Composable
private fun mobile(data: MangaObject, navigateTo: (String, SearchType) -> Unit) {
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

                else -> {

                }
            }
        }
        item {
            CommonName(data.russian, data.english)
        }
        item {
            CommonState(data.status)
        }
        item {
            CommonScore(data.score)
        }
        item {
            CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun desktop(data: MangaObject, navigateTo: (String, SearchType) -> Unit) {
    val new = false
    if (new) {
        FlowColumn {
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

                    is AsyncImagePainter.State.Empty -> {

                    }

                    is AsyncImagePainter.State.Error -> {

                    }

                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }
            Column {
                CommonName(data.russian, data.english)
                CommonState(data.status)
                CommonScore(data.score)
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(start = 10.dp)) {
                CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo)
            }
        }
    } else {
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

                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator()
                        }

                        else -> {

                        }
                    }
                }
                CommonName(data.russian, data.english)
                CommonState(data.status)
                CommonScore(data.score)
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                item { CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo) }
            }
        }
    }
}