package com.rcl.nextshiki.base.main.newspage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
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
import com.rcl.mr.SharedRes.strings.news_linked
import com.rcl.mr.SharedRes.strings.news_source
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.elements.contentscreens.AsyncPicture
import com.rcl.nextshiki.elements.contentscreens.htmlToAnnotatedString
import com.rcl.nextshiki.elements.contentscreens.rememberUriHandler
import com.rcl.nextshiki.elements.extractLink
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.topics.Linked
import com.rcl.nextshiki.models.topics.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsPageScreen(component: NewsPageComponent) {
    val topic = component.topic

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                navigationIcon = {
                    IconButton(onClick = component::navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    topic.topicTitle?.let {
                        Text(
                            it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        }) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            AdaptiveRow(
                firstRow = {
                    item("${topic.id} image") {
                        ImageBlock(
                            link = extractLink(topic.htmlFooter)
                        )
                    }
                    item("${topic.id} linked") {
                        LinkedBlock(linked = topic.linked)
                    }
                    item("${topic.id} user") {
                        UserBlock(user = topic.user)
                    }
                },
                secondRow = {
                    item(key = "${topic.id} text") {
                        DescriptionBlock(
                            navigate = component::navigateTo,
                            htmlBody = topic.htmlBody
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun ImageBlock(link: String?) = Box {
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalPlatformContext.current)
            .data(link)
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
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Error News Screen Icon"
            )
        }

        else -> {

        }
    }
}

@Composable
private fun LinkedBlock(linked: Linked?) {
    linked?.let {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "${news_linked.getLocalizableString()}:"
                )
                linked.name?.let { linkedName ->
                    Text(
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(start = 15.dp),
                        text = linkedName
                    )
                }
            }
        }
    }
}

@Composable
private fun UserBlock(user: User?) {
    user?.let {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "${news_source.getLocalizableString()}:"
                )

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
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Error News Screen user"
                                )
                            }

                            else -> {

                            }
                        }
                    }
                    user.nickname?.let { Text(it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun DescriptionBlock(navigate: (String, SearchType) -> Unit, htmlBody: String?) {
    val state = rememberRichTextState()

    val myUriHandler = rememberUriHandler(navigate)

    state.setConfig(
        linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onBackground)
    )

    htmlBody?.let { state.htmlToAnnotatedString(it) }

    CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
        Card(modifier = Modifier.fillMaxWidth()) {
            RichText(
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                state = state
            )
        }
    }
}
