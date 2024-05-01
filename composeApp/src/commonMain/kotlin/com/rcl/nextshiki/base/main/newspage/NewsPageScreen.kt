package com.rcl.nextshiki.base.main.newspage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.moko.MR.strings.news_linked
import com.rcl.moko.MR.strings.news_source
import com.rcl.nextshiki.elements.contentscreens.AsyncPicture
import com.rcl.nextshiki.elements.contentscreens.htmlToAnnotatedString
import com.rcl.nextshiki.models.topics.HotTopics
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NewsPageScreen(component: NewsPageComponent) {
    val topic = component.topic

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            windowInsets = WindowInsets(0),
            navigationIcon = {
                IconButton(onClick = component::navBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            title = {
                topic.topicTitle?.let { Text(it) }
            }
        )
    }) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            val size = calculateWindowSizeClass().widthSizeClass
            when (size) {
                Compact -> {
                    mobileUI(topic, component::extractLink, component::navigateByLink)
                }

                else -> {
                    desktopUI(topic, component::extractLink, component::navigateByLink)
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun desktopUI(topic: HotTopics, extractLink: (String?) -> String?, navigateByLink: (String) -> Unit) {
    Row(modifier = Modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            Box {
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(extractLink(topic.htmlFooter))
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
                        Icon(imageVector = Icons.Filled.Error, contentDescription = "Error News Screen Icon")
                    }

                    else -> {

                    }
                }
            }
            topic.linked?.let { linked ->
                Text("${stringResource(news_linked)}:")
                linked.name?.let { linkedName -> Text(modifier = Modifier.padding(start = 15.dp), text = linkedName) }
            }
            topic.user?.let { user ->
                Text(text = "${stringResource(news_source)}:")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalPlatformContext.current)
                                .data(user.image?.x160)
                                .size(Size.ORIGINAL)
                                .build()
                        )
                        when (painter.state) {
                            is AsyncImagePainter.State.Success -> {
                                Image(
                                    modifier = Modifier.clip(CircleShape),
                                    painter = painter,
                                    contentDescription = "News Screen user"
                                )
                            }

                            is AsyncImagePainter.State.Loading -> {
                                CircularProgressIndicator()
                            }

                            is AsyncImagePainter.State.Error -> {
                                Icon(imageVector = Icons.Filled.Error, contentDescription = "Error News Screen user")
                            }

                            else -> {

                            }
                        }
                    }
                    user.nickname?.let { Text(it) }
                }
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            item(key = "${topic.id} text") {
                val state = rememberRichTextState()
                val myUriHandler by remember {
                    mutableStateOf(object : UriHandler {
                        override fun openUri(uri: String) {
                            navigateByLink(uri)
                        }
                    })
                }

                state.setConfig(
                    linkColor = Color.Red
                )
                Napier.i("${topic.htmlBody}")
                topic.htmlBody?.let { state.htmlToAnnotatedString(it) }
                CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                    RichText(
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        state = state
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun mobileUI(topic: HotTopics, extractLink: (String?) -> String?, navigateByLink: (String) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item(key = "${topic.id} image") {
            Box {
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(extractLink(topic.htmlFooter))
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
                        Icon(imageVector = Icons.Filled.Error, contentDescription = "Error News Screen Icon")
                    }

                    else -> {

                    }
                }
            }
        }
        item(key = "${topic.id} linked") {
            topic.linked?.let { linked ->
                Text("${stringResource(news_linked)}:")
                linked.name?.let { linkedName -> Text(modifier = Modifier.padding(start = 15.dp), text = linkedName) }
            }
        }
        item(key = "${topic.id} user") {
            topic.user?.let { user ->
                Text(text = "${stringResource(news_source)}:")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalPlatformContext.current)
                                .data(user.image?.x160)
                                .size(Size.ORIGINAL)
                                .build()
                        )
                        when (painter.state) {
                            is AsyncImagePainter.State.Success -> {
                                Image(
                                    modifier = Modifier.clip(CircleShape),
                                    painter = painter,
                                    contentDescription = "News Screen user"
                                )
                            }

                            is AsyncImagePainter.State.Loading -> {
                                CircularProgressIndicator()
                            }

                            is AsyncImagePainter.State.Error -> {
                                Icon(imageVector = Icons.Filled.Error, contentDescription = "Error News Screen user")
                            }

                            else -> {

                            }
                        }
                    }
                    user.nickname?.let { Text(it) }
                }
            }
        }
        item(key = "${topic.id} text") {
            val state = rememberRichTextState()
            val myUriHandler by remember {
                mutableStateOf(object : UriHandler {
                    override fun openUri(uri: String) {
                        navigateByLink(uri)
                    }
                })
            }

            state.setConfig(
                linkColor = Color.Blue.harmonize(
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    matchSaturation = true
                )
            )
            topic.htmlBody?.let { state.htmlToAnnotatedString(it) }
            CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                RichText(
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    state = state
                )
            }
        }
    }
}