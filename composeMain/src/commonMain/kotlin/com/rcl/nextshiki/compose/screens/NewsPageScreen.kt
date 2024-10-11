package com.rcl.nextshiki.compose.screens

import Nextshiki.resources.BuildConfig
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.nextshiki.SharedRes.strings.news_linked
import com.rcl.nextshiki.SharedRes.strings.news_source
import com.rcl.nextshiki.components.newscomponent.newspage.NewsPageComponent
import com.rcl.nextshiki.compose.AdaptiveRow
import com.rcl.nextshiki.compose.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.compose.withLocalSharedTransition
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.topics.Linked
import com.rcl.nextshiki.models.topics.User
import com.rcl.nextshiki.utils.extractLink
import com.rcl.nextshiki.utils.getValidUrlByLink
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun NewsPageScreen(
    component: NewsPageComponent
) {
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
                    withLocalSharedTransition {
                        topic.topicTitle?.let {
                            Text(
                                it,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.sharedBounds(
                                    rememberSharedContentState("${topic.id} news title"),
                                    LocalAnimatedVisibilityScope.current
                                )
                            )
                        }
                    }
                }
            )
        }) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            AdaptiveRow(
                {
                    item("${topic.id} image") {
                        topic.id?.let {
                            ImageBlock(
                                link = extractLink(topic.htmlFooter, BuildConfig.DOMAIN),
                                argId = it
                            )
                        }
                    }
                    item("${topic.id} linked") {
                        LinkedBlock(linked = topic.linked)
                    }
                    item("${topic.id} user") {
                        topic.id?.let { UserBlock(user = topic.user, argId = it) }
                    }
                },
                {
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ImageBlock(argId: Int, link: String?) = Box {
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalPlatformContext.current)
            .data(link)
            .size(Size.ORIGINAL)
            .build()
    )
    val painterState by painter.state.collectAsState()
    when (painterState) {
        is State.Success -> {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                withLocalSharedTransition {
                    Image(
                        painter = painter,
                        contentDescription = "News Screen image",
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState("$argId news image"),
                            LocalAnimatedVisibilityScope.current
                        )
                    )
                }
            }
        }

        is State.Loading -> {
            CircularProgressIndicator()
        }

        is State.Error -> {
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
                    text = "${stringResource(news_linked)}:"
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun UserBlock(argId: Int, user: User?) {
    user?.let {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "${stringResource(news_source)}:"
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    withLocalSharedTransition {
                        Box {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalPlatformContext.current)
                                    .data(user.image?.x160)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )
                            val painterState by painter.state.collectAsState()
                            when (painterState) {
                                is State.Success -> {

                                    Image(
                                        modifier =
                                        Modifier
                                            .clip(CircleShape)
                                            .sharedBounds(
                                                sharedContentState =
                                                rememberSharedContentState("$argId news source image"),
                                                animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                                                clipInOverlayDuringTransition = OverlayClip(
                                                    CircleShape
                                                ),
                                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                            ),
                                        painter = painter,
                                        contentDescription = "News Screen user"
                                    )
                                }

                                is State.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is State.Error -> {
                                    Icon(
                                        imageVector = Icons.Filled.Error,
                                        contentDescription = "Error News Screen user"
                                    )
                                }

                                else -> {

                                }
                            }
                        }
                        user.nickname?.let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState =
                                        rememberSharedContentState("$argId news source nickname"),
                                        animatedVisibilityScope = LocalAnimatedVisibilityScope.current
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun DescriptionBlock(navigate: (SearchCardModel, SearchType) -> Unit, htmlBody: String?) {
    val state = rememberRichTextState()

    val myUriHandler = rememberUriHandler(navigate)

    state.config.linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onBackground)

    htmlBody?.let { state.setHtml(it) }

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

@Composable
fun rememberUriHandler(navigateTo: (SearchCardModel, SearchType) -> Unit) = remember {
    val excludedUrl = listOf(
        "animes/studio/",
        "mangas/studio/"
    )
    object : UriHandler {
        override fun openUri(uri: String) {
            val fixedLink = getValidUrlByLink(uri, BuildConfig.DOMAIN)

            if (excludedUrl.any { fixedLink.contains(it) }) return

            val list = fixedLink.split("/")

            when (list[3]) {
                "animes" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.Anime
                    ), SearchType.Anime
                )

                "mangas" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.Manga
                    ), SearchType.Manga
                )

                "ranobe" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.Ranobe
                    ), SearchType.Ranobe
                )

                "people" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.People
                    ), SearchType.People
                )

                "users" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.Users
                    ), SearchType.Users
                )

                "characters" -> navigateTo(
                    SearchCardModel(
                        id = list[4].split("-")[0].toIntOrNull() ?: 0,
                        searchType = SearchType.Characters
                    ), SearchType.Characters
                )

                else -> {}
            }
        }
    }
}