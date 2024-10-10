package com.rcl.nextshiki.compose.screens.content

import Nextshiki.resources.BuildConfig
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.materialkolor.ktx.harmonize
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.rcl.nextshiki.SharedRes.strings.common_roles
import com.rcl.nextshiki.SharedRes.strings.content_franchise
import com.rcl.nextshiki.SharedRes.strings.content_name
import com.rcl.nextshiki.SharedRes.strings.description_in_object
import com.rcl.nextshiki.SharedRes.strings.people_participation
import com.rcl.nextshiki.SharedRes.strings.score_in_object
import com.rcl.nextshiki.SharedRes.strings.source
import com.rcl.nextshiki.SharedRes.strings.status_anons
import com.rcl.nextshiki.SharedRes.strings.status_discontinued
import com.rcl.nextshiki.SharedRes.strings.status_in_object
import com.rcl.nextshiki.SharedRes.strings.status_ongoing
import com.rcl.nextshiki.SharedRes.strings.status_paused
import com.rcl.nextshiki.SharedRes.strings.status_released
import com.rcl.nextshiki.SharedRes.strings.unknown
import com.rcl.nextshiki.compose.AdaptiveRow
import com.rcl.nextshiki.compose.CommonCarouselList
import com.rcl.nextshiki.compose.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.compose.RatingBar
import com.rcl.nextshiki.compose.getLangRes
import com.rcl.nextshiki.compose.screens.rememberUriHandler
import com.rcl.nextshiki.compose.withLocalSharedTransition
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.ISearchDescriptions
import com.rcl.nextshiki.models.searchobject.ISearchFranchise
import com.rcl.nextshiki.models.searchobject.ISearchParticipation
import com.rcl.nextshiki.models.searchobject.ISearchRoles
import com.rcl.nextshiki.models.searchobject.ISearchScore
import com.rcl.nextshiki.models.searchobject.ISearchStatus
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.people.Works
import com.rcl.nextshiki.models.universal.Image
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalRichTextApi::class)
@Composable
fun SearchedContentCommon(
    searchType: SearchType,
    currentCode: String,
    data: CommonSearchInterface,
    navigateTo: (SearchCardModel, SearchType) -> Unit
) {
    withLocalSharedTransition {
        AdaptiveRow(
            {
                item(key = "${data.id} profile icon") {
                    Box {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalPlatformContext.current)
                                .data(BuildConfig.DOMAIN + (data.image?.original))
                                .size(Size.ORIGINAL)
                                .build()
                        )
                        val painterState by painter.state.collectAsState()
                        when (painterState) {
                            is Success -> {
                                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Searched element image ${data.id}",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .sharedBounds(
                                                rememberSharedContentState("searched_card_${data.id}_image"),
                                                LocalAnimatedVisibilityScope.current,
                                            )
                                            .width(maxWidth / 2)
                                            .align(Alignment.Center)
                                            .clip(RoundedCornerShape(10.dp))
                                            .aspectRatio(1f),
                                    )
                                }
                            }

                            is Loading -> {
                                CircularProgressIndicator()
                            }

                            is AsyncImagePainter.State.Error -> {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Error Icon"
                                )
                            }

                            else -> {

                            }
                        }
                    }
                }
                item(key = "${data.id} name") {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = stringResource(content_name),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            data.name?.let {
                                data.russian?.let { english ->
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp)
                                            .sharedBounds(
                                                rememberSharedContentState("searched_card_${data.id}_name"),
                                                LocalAnimatedVisibilityScope.current,
                                            ),
                                        style = MaterialTheme.typography.headlineMedium,
                                        text = getLangRes(
                                            currentCode = currentCode,
                                            english = it,
                                            russian = english,
                                        ),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                if (data is ISearchStatus)
                    item(key = "${data.id} status") {
                        val statusRes = when (data.status) {
                            "released" -> status_released
                            "anons" -> status_anons
                            "ongoing" -> status_ongoing
                            "paused" -> status_paused
                            "discontinued" -> status_discontinued
                            else -> unknown
                        }
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = stringResource(status_in_object),
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = MaterialTheme.typography.headlineMedium,
                                    text = stringResource(statusRes)
                                )
                            }
                        }
                    }
                item(key = "${data.id} score") {
                    if (data is ISearchScore) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    style = MaterialTheme.typography.headlineSmall,
                                    text = "${stringResource(score_in_object)}: ${data.score}"
                                )
                                RatingBar(
                                    modifier = Modifier.padding(start = 10.dp),
                                    imageVector = Icons.Filled.StarRate,
                                    maxRating = 10,
                                    rating = data.score?.toFloatOrNull() ?: 0f,
                                )
                            }
                        }
                    }
                }
            },
            {
                if (data is ISearchDescriptions) {
                    item(key = "${data.id} description") {
                        if (!data.descriptionHtml.isNullOrEmpty()) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        style = MaterialTheme.typography.headlineSmall,
                                        text = stringResource(description_in_object)
                                    )

                                    val myUriHandler = rememberUriHandler(navigateTo)

                                    val state = rememberRichTextState()
                                    state.config.linkColor = Color.Blue.harmonize(
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        matchSaturation = true
                                    )
                                    state.setHtml(data.descriptionHtml!!)

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
                                        text = "${stringResource(source)}: ${
                                            data.descriptionSource ?: stringResource(unknown)
                                        }"
                                    )
                                }
                            }
                        }
                    }
                }
                if (data is ISearchRoles) {
                    item(key = "${data.id} roles") {
                        val mainCharList = data.rolesList
                            .filter { rolesClass ->
                                rolesClass.roles.contains("Main") || rolesClass.roles.contains(
                                    "Supporting"
                                )
                            }
                            .take(10)

                        val characters = mainCharList.mapNotNull { it.character }

                        CommonCarouselList(
                            currentCode = currentCode,
                            searchType = SearchType.Characters,
                            navigateTo = navigateTo,
                            hasNext = mainCharList.size > 11,
                            title = common_roles,
                            carouselList = characters,
                        )
                    }
                }
                if (data is ISearchFranchise) {
                    item(key = "${data.id} franchise") {
                        if (data.franchiseModel != null && data.franchiseModel!!.nodes.isNotEmpty()) {
                            val nodes = data.franchiseModel!!.nodes.take(10)
                            val commonSearchList = nodes.map { franchize ->
                                object : CommonSearchInterface {
                                    override val id: Int? = franchize.id
                                    override val name: String? = franchize.name
                                    override val russian: String? = franchize.name
                                    override val url: String? = franchize.url
                                    override val image: Image? = Image(original = franchize.imageUrl, preview = franchize.imageUrl, x160 = franchize.imageUrl)
                                    override val searchType: SearchType = searchType
                                }
                            }

                            CommonCarouselList(
                                currentCode = currentCode,
                                searchType = searchType,
                                navigateTo = navigateTo,
                                hasNext = data.franchiseModel!!.nodes.size > 11,
                                title = content_franchise,
                                carouselList = commonSearchList,
                            )
                        }
                    }
                }
                if (data is ISearchParticipation) {
                    item(key = "${data.id} participation") {
                        val list = convertWorksToModels(data.works)

                        CommonCarouselList(
                            navigateTo = navigateTo,
                            title = people_participation,
                            carouselList = list,
                            hasNext = list.size > 11,
                            currentCode = currentCode,
                            searchType = searchType
                        )
                    }
                }
            }
        )
    }
}

private fun convertWorksToModels(worksList: List<Works>): List<CommonSearchInterface> {
    val commonSearchList = arrayListOf<CommonSearchInterface>()

    for (works in worksList) {
        val model = works.anime ?: works.manga
        val type = if (works.anime == null) SearchType.Manga else SearchType.Anime

        if (model != null) {

            commonSearchList.add(
                object : CommonSearchInterface {
                    override val id: Int? = model.id
                    override val name: String? = model.name
                    override val russian: String? = model.russian
                    override val url: String? = model.url
                    override val image: Image? = model.image
                    override val searchType: SearchType = type
                }
            )
        }
    }

    return commonSearchList
}