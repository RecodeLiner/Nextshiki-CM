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
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.collections.immutable.persistentListOf

@Composable
fun UserScreen(data: CommonSearchInterface, navigateTo: (SearchCardModel, SearchType) -> Unit) {
    AdaptiveRow(
        firstRow = {
            item(key = "user ${data.id} profile icon") {
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
                        is AsyncImagePainter.State.Success -> {
                            AsyncPicture(painter)
                        }

                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator()
                        }

                        is Error -> {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error user Screen Icon"
                            )
                        }

                        else -> {

                        }
                    }
                }
            }
            item(key = "user ${data.id} name") {
                if (data is UserObject) {
                    CommonName(data.russian, persistentListOf(data.nickname, data.name))
                }
            }
        },
        secondRow = {
            item(key = "user ${data.id} description") {
                if (data is UserObject) {
                    CommonDescription(data.aboutHtml, null, navigateTo)
                }
            }
        }
    )
}
