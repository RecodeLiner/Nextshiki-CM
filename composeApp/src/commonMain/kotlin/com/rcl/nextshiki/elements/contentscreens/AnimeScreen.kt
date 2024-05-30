package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import coil3.compose.AsyncImagePainter.State.Error
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AnimeScreen(data: AnimeObject, navigateTo: (String, SearchType) -> Unit) {
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
                    when (painter.state) {
                        is Success -> {
                            AsyncPicture(painter)
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
                CommonName(data.russian, data.english.toPersistentList())
            }
            item(key = "anime ${data.id} status") {
                CommonState(data.status)
            }
            item(key = "anime ${data.id} score") {
                CommonScore(data.score)
            }
        },
        secondRow = {
            item(key = "anime ${data.id} description") {
                CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo)
            }
            item(key = "anime ${data.id} roles") {
                CommonRoles(data.rolesList.toPersistentList(), navigateTo)
            }
            item(key = "anime ${data.id} franchise") {
                CommonFranchise(data.franchiseModel, navigateTo, SearchType.Anime)
            }
        }
    )
}