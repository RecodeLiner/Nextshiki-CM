package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State.Error
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.elements.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.elements.withLocalSharedTransition
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimeScreen(data: CommonSearchInterface, navigateTo: (SearchCardModel, SearchType) -> Unit) {
    AdaptiveRow(
        firstRow = {
            item(key = "anime ${data.id} profile icon") {
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
                                withLocalSharedTransition {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Calendar preview image",
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
                        }

                        is Loading -> {
                            CircularProgressIndicator()
                        }

                        is Error -> {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error Anime Screen Icon"
                            )
                        }

                        else -> {

                        }
                    }
                }
            }
            item(key = "anime ${data.id} name") {
                if (data is AnimeObject) {
                    CommonName(
                        russian = data.russian,
                        english = data.english.toPersistentList(),
                    )
                }
            }
            item(key = "anime ${data.id} status") {
                if (data is AnimeObject) {
                    CommonState(data.status)
                }
            }
            item(key = "anime ${data.id} score") {
                if (data is AnimeObject) {
                    CommonScore(data.score)
                }
            }
        },
        secondRow = {
            item(key = "anime ${data.id} description") {
                if (data is AnimeObject) {
                    CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo)
                }
            }
            item(key = "anime ${data.id} roles") {
                if (data is AnimeObject) {
                    CommonRoles(data.rolesList.toPersistentList(), navigateTo)
                }
            }
            item(key = "anime ${data.id} franchise") {
                if (data is AnimeObject) {
                    CommonFranchise(data.franchiseModel, navigateTo, SearchType.Anime)
                }
            }
        }
    )
}
