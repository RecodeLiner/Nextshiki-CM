package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.compose.AsyncImagePainter.State.Error
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MangaScreen(data: CommonSearchInterface, navigateTo: (SearchCardModel, SearchType) -> Unit) {
    AdaptiveRow(
        firstRow = {
            item(key = "manga ${data.id} profile icon") {
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
                            AsyncPicture(painter)
                        }

                        is Loading -> {
                            CircularProgressIndicator()
                        }

                        is Error -> {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error Manga Screen Icon"
                            )
                        }

                        else -> {

                        }
                    }
                }
            }
            item(key = "manga ${data.id} name") {
                if (data is MangaObject) {
                    CommonName(data.russian, data.english.toPersistentList())
                }
            }
            item(key = "manga ${data.id} status") {
                if (data is MangaObject) {
                    CommonState(data.status)
                }
            }
            item(key = "manga ${data.id} score") {
                if (data is MangaObject) {
                    CommonScore(data.score)
                }
            }
        },
        secondRow = {
            item(key = "manga ${data.id} description") {
                if (data is MangaObject) {
                    CommonDescription(data.descriptionHtml, data.descriptionSource, navigateTo)
                }
            }
            item(key = "manga ${data.id} description") {
                if (data is MangaObject) {
                    CommonFranchise(data.franchiseModel, navigateTo, SearchType.Manga)
                }
            }
        }
    )
}
