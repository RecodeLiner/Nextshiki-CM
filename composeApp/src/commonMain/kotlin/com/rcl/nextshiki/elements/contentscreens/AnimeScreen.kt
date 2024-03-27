package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.moko.MR.strings.description_in_object
import com.rcl.moko.MR.strings.score_in_object
import com.rcl.moko.MR.strings.source
import com.rcl.moko.MR.strings.text_empty
import com.rcl.moko.MR.strings.unknown
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AnimeScreen(data: AnimeObject, navigateTo: (Int, SearchType) -> Unit) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        Compact -> {
            mobile(data, navigateTo)
        }

        else -> {
            desktop(data, navigateTo)
        }
    }
}

@Composable
fun mobile(data: AnimeObject, navigateTo: (Int, SearchType) -> Unit) {
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
                is Success -> {
                    AnimePicture(painter)
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
        item {
            AnimeName(data)
        }
        item {
            AnimeScore(data)
        }
        item {
            AnimeDescription(data, navigateTo)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun desktop(data: AnimeObject, navigateTo: (Int, SearchType) -> Unit) {
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
                    is Success -> {
                        AnimePicture(painter)
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
                AnimeName(data)
                AnimeScore(data)
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(start = 10.dp)) {
                AnimeDescription(data, navigateTo)
            }
        }
    } else {
        Row(modifier = Modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(BuildConfig.DOMAIN + (data.image?.original))
                            .size(Size.ORIGINAL)
                            .build()
                    )
                    when (painter.state) {
                        is Success -> {
                            AnimePicture(painter)
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
                AnimeName(data)
                AnimeScore(data)
            }
            LazyColumn {
                item { AnimeDescription(data, navigateTo) }
            }
        }
    }
}

@Composable
private fun AnimePicture(painter: Painter) {
    BoxWithConstraints {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(maxWidth / 2)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .aspectRatio(1f),
        )
    }
}

@Composable
private fun AnimeName(data: AnimeObject) {
    Text(
        style = MaterialTheme.typography.headlineSmall,
        text = when (Locale.current.language) {
            "ru" -> data.russian ?: ""
            else -> data.english[0] ?: ""
        },
        overflow = TextOverflow.Ellipsis
    )

    //TODO: Add another names
    /*if (data.english.size != 1) {

    }*/
}

@Composable
private fun AnimeScore(data: AnimeObject) {
    Row {
        Text(
            text = stringResource(score_in_object)
        )
        data.score?.let {
            Text(
                text = it,
            )
        }
        Icon(
            Icons.Default.Star,
            contentDescription = "Star icon in content"
        )
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun AnimeDescription(data: AnimeObject, navigateTo: (Int, SearchType) -> Unit) {
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(description_in_object)
        )
        if (data.descriptionHtml != null) {

            val myUriHandler by remember {
                mutableStateOf(object : UriHandler {
                    override fun openUri(uri: String) {
                        val list = uri.split("/")
                        if (list.contains("anime")) {
                            val index = list.indexOf("anime").dec()
                            Napier.i("index: $index")
                            list[index].toIntOrNull()?.let { navigateTo(it, SearchType.Anime) }
                        }
                        else {
                            Napier.i("not $uri")
                        }
                    }
                })
            }

            val state = rememberRichTextState()
            state.setConfig(
                linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true)
            )
            state.htmlToAnnotatedString(data.descriptionHtml)

            CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                RichText(
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    state = state
                )
            }

            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${stringResource(source)}: ${
                    data.descriptionSource ?: stringResource(
                        unknown
                    )
                }"
            )
        } else {
            Text(
                text = stringResource(text_empty)
            )
        }
    }
}

fun RichTextState.htmlToAnnotatedString(html: String) {
    this.setHtml(html)
}
