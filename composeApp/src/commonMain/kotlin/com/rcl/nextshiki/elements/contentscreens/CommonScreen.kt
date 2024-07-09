package com.rcl.nextshiki.elements.contentscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
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
import com.rcl.mr.SharedRes.strings.common_roles
import com.rcl.mr.SharedRes.strings.content_franchise
import com.rcl.mr.SharedRes.strings.content_name
import com.rcl.mr.SharedRes.strings.description_in_object
import com.rcl.mr.SharedRes.strings.more
import com.rcl.mr.SharedRes.strings.picture_error
import com.rcl.mr.SharedRes.strings.score_in_object
import com.rcl.mr.SharedRes.strings.source
import com.rcl.mr.SharedRes.strings.status_anons
import com.rcl.mr.SharedRes.strings.status_discontinued
import com.rcl.mr.SharedRes.strings.status_in_object
import com.rcl.mr.SharedRes.strings.status_ongoing
import com.rcl.mr.SharedRes.strings.status_paused
import com.rcl.mr.SharedRes.strings.status_released
import com.rcl.mr.SharedRes.strings.unknown
import com.rcl.nextshiki.base.profile.mainprofile.profile.RatingBar
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.getValidImageUrl
import com.rcl.nextshiki.elements.getValidUrlByLink
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.franchise.FranchiseModel
import com.rcl.nextshiki.models.franchise.Nodes
import com.rcl.nextshiki.models.searchobject.RolesClass
import com.rcl.nextshiki.models.universal.CarouselModel
import dev.icerock.moko.resources.StringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
                .align(Alignment.Center)
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f),
        )
    }
}

@Stable
@Composable
fun CommonName(russian: String?, english: ImmutableList<String?>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = content_name.getLocalizableString(),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.headlineMedium,
                text = getLangRes(
                    english = english[0], russian = if (russian.isNullOrEmpty()) {
                        english[0]
                    } else russian
                ) ?: "",
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CommonScore(score: String?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = "${score_in_object.getLocalizableString()}: $score"
            )
            RatingBar(
                modifier = Modifier.padding(start = 10.dp),
                imageVector = Icons.Filled.StarRate,
                maxRating = 10,
                rating = score?.toFloatOrNull() ?: 0f,
            )
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun CommonDescription(
    descriptionHtml: String?, descriptionSource: String?, navigateTo: (String, SearchType) -> Unit
) {
    if (!descriptionHtml.isNullOrEmpty()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = description_in_object.getLocalizableString()
                )

                val myUriHandler = rememberUriHandler(navigateTo)

                val state = rememberRichTextState()
                state.setConfig(
                    linkColor = Color.Blue.harmonize(
                        MaterialTheme.colorScheme.onPrimaryContainer, matchSaturation = true
                    )
                )
                state.htmlToAnnotatedString(descriptionHtml)

                CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
                    RichText(
                        modifier = Modifier.padding(start = 10.dp),
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
            }
        }
    }
}

val excludedUrl = listOf(
    "animes/studio/",
    "mangas/studio/"
)

@Composable
fun rememberUriHandler(navigateTo: (String, SearchType) -> Unit) = remember {
    object : UriHandler {
        override fun openUri(uri: String) {
            val fixedLink = getValidUrlByLink(uri)

            if (excludedUrl.any { fixedLink.contains(it) }) return

            val list = fixedLink.split("/")

            when (list[3]) {
                "animes" -> navigateTo(list[4].split("-")[0], SearchType.Anime)
                "mangas" -> navigateTo(list[4].split("-")[0], SearchType.Manga)
                "ranobe" -> navigateTo(list[4].split("-")[0], SearchType.Ranobe)
                "people" -> navigateTo(list[4].split("-")[0], SearchType.People)
                "users" -> navigateTo(list[4].split("-")[0], SearchType.Users)
                "characters" -> navigateTo(list[4].split("-")[0], SearchType.Characters)
                else -> {}
            }
        }
    }
}

@Composable
fun CommonRoles(
    rolesList: ImmutableList<RolesClass>,
    navigateTo: (String, SearchType) -> Unit)
{
    val mainCharList = rolesList.filter { rolesClass ->
        rolesClass.roles.contains("Main") || rolesClass.roles.contains("Supporting")
    }.toPersistentList()

    val size = if (mainCharList.size < 10) {
        mainCharList.size
    } else {
        10
    }
    CommonCarouselList(
        navigateTo = navigateTo,
        title = common_roles,
        carouselList = mainCharList.subList(0, size).toCarouselModel(
            idSelector = { it.character?.id },
            imageSelector = { it.character?.image?.let { img -> getValidImageUrl(img) } },
            englishNameSelector = { persistentListOf(it.character?.name) },
            russianNameSelector = { persistentListOf(it.character?.name) },
            searchTypeSelector = { SearchType.Characters },
            urlSelector = { it.character?.url }),
        hasNext = mainCharList.size > 11
    )
}

@Composable
fun CommonFranchise(
    franchiseModel: FranchiseModel?,
    navigateTo: (String, SearchType) -> Unit,
    type: SearchType
) {
    if (franchiseModel != null && franchiseModel.nodes.isNotEmpty()) {
        val nodes = franchiseModel.nodes.toPersistentList()
        val size = if (nodes.size < 10) nodes.size else 10
        val carouselList = createCarouselList(nodes, size, type)

        CommonCarouselList(
            navigateTo = navigateTo,
            title = content_franchise,
            hasNext = franchiseModel.nodes.size > 11,
            carouselList = carouselList
        )
    }
}

@Composable
private fun createCarouselList(
    nodes: ImmutableList<Nodes>,
    size: Int,
    type: SearchType
): ImmutableList<CarouselModel> {
    return nodes.subList(0, size).toCarouselModel(
        englishNameSelector = { persistentListOf(it.name, it.kind) },
        russianNameSelector = { persistentListOf(it.name, it.kind) },
        idSelector = { it.id },
        imageSelector = { it.imageUrl?.let { url -> getValidUrlByLink(url) } },
        searchTypeSelector = { type },
        urlSelector = { it.url }
    )
}

@Composable
fun CommonState(status: String?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = status_in_object.getLocalizableString(),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.headlineMedium,
                text = when (status) {
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
}

@Composable
fun CommonCarouselList(
    navigateTo: (String, SearchType) -> Unit,
    title: StringResource,
    carouselList: ImmutableList<CarouselModel>,
    hasNext: Boolean
) {
    val rowState = rememberLazyListState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(title.getLocalizableString(), style = MaterialTheme.typography.headlineSmall)
            CarouselCard(rowState, carouselList, navigateTo, hasNext)
        }
    }
}

@Composable
private fun CarouselCard(
    rowState: LazyListState,
    carouselList: ImmutableList<CarouselModel>,
    navigateTo: (String, SearchType) -> Unit,
    hasNext: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        colors = CardDefaults.cardColors()
            .copy(
                MaterialTheme.colorScheme.primaryContainer.harmonize(
                    MaterialTheme.colorScheme.secondary
                )
            )
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
                CarouselItem(carouselItem, navigateTo)
            }

            if (hasNext) {
                item(key = "moreFriends") {
                    MoreFriendsItem()
                }
            }
        }
    }
}

@Composable
private fun CarouselItem(carouselItem: CarouselModel, navigateTo: (String, SearchType) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.width(50.dp).noRippleClickable {
            if (carouselItem.id != null) {
                navigateTo(carouselItem.id.toString(), carouselItem.contentType)
            }
        }
    ) {
        carouselItem.image?.let { imageLink ->
            Box { CarouselIcon(url = getValidUrlByLink(imageLink)) }
        }

        for ((eng, rus) in carouselItem.englishName.zip(carouselItem.russianName)) {
            getLangRes(english = eng, russian = rus)?.let { name ->
                Text(text = name, overflow = TextOverflow.Ellipsis, maxLines = 2)
            }
        }
    }
}

@Composable
private fun MoreFriendsItem() {
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

@Composable
fun <T> ImmutableList<T>.toCarouselModel(
    idSelector: (T) -> Int?,
    searchTypeSelector: (T) -> SearchType,
    englishNameSelector: (T) -> ImmutableList<String?>,
    russianNameSelector: (T) -> ImmutableList<String?>,
    imageSelector: (T) -> String?,
    urlSelector: (T) -> String?
): ImmutableList<CarouselModel> {
    return this.map { content ->
        CarouselModel(
            id = idSelector(content),
            englishName = englishNameSelector(content),
            russianName = russianNameSelector(content),
            image = imageSelector(content),
            contentType = searchTypeSelector(content),
            url = urlSelector(content)
        )
    }.toPersistentList()
}

@Composable
private fun CarouselIcon(url: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current).data(url).size(Size.ORIGINAL).build()
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
            Image(
                painter = painter,
                modifier = Modifier.aspectRatio(ratio = 1f),
                contentDescription = "carousel pic"
            )
        }

        else -> {}
    }
}

fun RichTextState.htmlToAnnotatedString(html: String) {
    this.setHtml(html)
}
