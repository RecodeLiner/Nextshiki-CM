package com.rcl.nextshiki.elements.contentscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.*
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
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.mr.MR.strings.description_in_object
import com.rcl.mr.MR.strings.more
import com.rcl.mr.MR.strings.picture_error
import com.rcl.mr.MR.strings.profile_friends
import com.rcl.mr.MR.strings.score_in_object
import com.rcl.mr.MR.strings.source
import com.rcl.mr.MR.strings.status_anons
import com.rcl.mr.MR.strings.status_discontinued
import com.rcl.mr.MR.strings.status_in_object
import com.rcl.mr.MR.strings.status_ongoing
import com.rcl.mr.MR.strings.status_paused
import com.rcl.mr.MR.strings.status_released
import com.rcl.mr.MR.strings.text_empty
import com.rcl.mr.MR.strings.unknown
import com.rcl.nextshiki.base.profile.mainprofile.profile.RatingBar
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.base.search.mainsearchscreen.getValidImageUrl
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.searchobject.RolesClass
import com.rcl.nextshiki.models.universal.CarouselModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun AsyncPicture(painter: Painter) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(maxWidth / 2)
                .align(Alignment.Center).clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f),
        )
    }
}

@Stable
@Composable
fun CommonName(russian: String?, english: ImmutableList<String?>) {
    Text(
        style = MaterialTheme.typography.headlineSmall, text = when (Locale.current.language) {
            "ru" -> russian ?: english[0] ?: ""
            else -> english[0] ?: ""
        }, overflow = TextOverflow.Ellipsis
    )

    //TODO: Add another names
    /*if (data.english.size != 1) {

    }*/
}

@Composable
fun CommonScore(score: String?) {
    Column {
        Row {
            Text(
                text = "${score_in_object.getLocalizableString()} "
            )
            score?.let {
                Text(
                    text = it,
                )
            }
        }
        RatingBar(
            imageVector = Icons.Filled.StarRate,
            maxRating = 10,
            rating = score?.toFloatOrNull() ?: 0f,
        )
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun CommonDescription(descriptionHtml: String?, descriptionSource: String?, navigateTo: (String, SearchType) -> Unit) {
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge, text = description_in_object.getLocalizableString()
        )
        if (descriptionHtml != null) {

            val myUriHandler by remember {
                mutableStateOf(object : UriHandler {
                    override fun openUri(uri: String) {
                        val list = uri.split("/")
                        when (list[3]) {
                            "animes" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Anime)
                            }

                            "mangas" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Manga)
                            }

                            "ranobe" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Ranobe)
                            }

                            "people" -> {
                                navigateTo(list[4].split("-")[0], SearchType.People)
                            }

                            "users" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Users)
                            }

                            "characters" -> {
                                navigateTo(list[4].split("-")[0], SearchType.Characters)
                            }

                            else -> {
                                //Napier.i("uri - $uri, part - ${list[3]}")
                            }
                        }
                    }
                })
            }

            val state = rememberRichTextState()
            state.setConfig(
                linkColor = Color.Blue.harmonize(MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true)
            )
            state.htmlToAnnotatedString(descriptionHtml)

            CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                RichText(
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    state = state
                )
            }

            Text(
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                text = "${source.getLocalizableString()}: ${
                    descriptionSource ?: unknown.getLocalizableString()
                }"
            )
        } else {
            Text(
                text = text_empty.getLocalizableString()
            )
        }
    }
}

@Composable
fun CommonRoles(rolesList: ImmutableList<RolesClass>) {
    val mainCharList = rolesList.filter { rolesClass ->
        rolesClass.roles.contains("Main") || rolesClass.roles.contains("Supporting")
    }.toPersistentList()
    CommonCarouselList(mainCharList.subList(0, 10).toCarouselModel(
        idSelector = { it.character?.id },
        imageSelector = { it.character?.image?.let { img -> getValidImageUrl(img) } },
        englishNameSelector = { it.character?.name },
        russianNameSelector = { it.character?.name },
        urlSelector = { it.character?.url }
    ), mainCharList.size > 11
    )
}

@Composable
fun CommonState(status: String?) {
    Row {
        Text("${status_in_object.getLocalizableString()} ")
        Text(
            when (status) {
                "released" -> status_released.getLocalizableString()
                "anons" -> status_anons.getLocalizableString()
                "ongoing" -> status_ongoing.getLocalizableString()
                "paused" -> status_paused.getLocalizableString()
                "discontinued" -> status_discontinued.getLocalizableString()
                else -> unknown.getLocalizableString()
            }
        )
    }
}

@Composable
fun CommonCarouselList(carouselList: ImmutableList<CarouselModel>, hasNext: Boolean) {
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(10.dp)) {
            Text(profile_friends.getLocalizableString(), style = MaterialTheme.typography.headlineSmall)
            Card(
                colors = CardDefaults.cardColors()
                    .copy(MaterialTheme.colorScheme.primaryContainer.harmonize(MaterialTheme.colorScheme.secondary))
            ) {
                LazyRow(
                    state = rowState,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(5.dp).padding(start = 10.dp).draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            coroutineScope.launch {
                                rowState.scrollBy(-delta)
                            }
                        },
                    ),
                ) {
                    items(carouselList, key = { it.id ?: "Unexpected carousel item" }) { carouselItem ->
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.width(50.dp)) {
                            carouselItem.image?.let { imageLink ->
                                Box { CarouselIcon(url = imageLink) }
                            }
                            getLangRes(
                                english = carouselItem.englishName,
                                russian = carouselItem.russianName
                            )?.let { name ->
                                Text(
                                    name,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if (hasNext) {
                        item(key = "moreFriends") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = "more friends",
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(more.getLocalizableString(), maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> ImmutableList<T>.toCarouselModel(
    idSelector: (T) -> Int?,
    englishNameSelector: (T) -> String?,
    russianNameSelector: (T) -> String?,
    imageSelector: (T) -> String?,
    urlSelector: (T) -> String?
): ImmutableList<CarouselModel> {
    return this.map { content ->
        CarouselModel(
            id = idSelector(content),
            englishName = englishNameSelector(content),
            russianName = russianNameSelector(content),
            image = imageSelector(content),
            url = urlSelector(content)
        )
    }.toPersistentList()
}

@Composable
private fun CarouselIcon(url: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
    when (painter.state) {
        is AsyncImagePainter.State.Error -> {
            Column {
                Icon(Icons.Default.Error, contentDescription = "error in carousel")
                Text(picture_error.getLocalizableString())
            }
        }

        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(painter = painter, modifier = Modifier.aspectRatio(ratio = 1f), contentDescription = "carousel pic")
        }

        else -> {}
    }
}

fun RichTextState.htmlToAnnotatedString(html: String) {
    this.setHtml(html)
}