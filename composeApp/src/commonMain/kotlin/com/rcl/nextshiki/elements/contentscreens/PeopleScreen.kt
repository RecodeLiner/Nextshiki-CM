package com.rcl.nextshiki.elements.contentscreens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.mr.MR.strings.people_participation
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.AdaptiveRow
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.people.Works
import com.rcl.nextshiki.models.universal.CarouselModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PeopleScreen(data: PeopleObject, navigateTo: (String, SearchType) -> Unit) {
    AdaptiveRow(
        firstRow = {
            item("People ${data.id} profile icon") {
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(BuildConfig.DOMAIN + (data.image?.original))
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
                            contentDescription = "Error People Screen Icon"
                        )
                    }

                    else -> {

                    }
                }
            }
            item("People ${data.id} description") {
                CommonName(data.russian, persistentListOf(data.name))
            }
        },
        secondRow = {
            item("People ${data.id} participation") {
                val list = convertWorksToCarouselModels(data.works)
                CommonCarouselList(
                    navigateTo = navigateTo,
                    title = people_participation,
                    carouselList = list,
                    hasNext = list.size > 11
                )
            }
        }
    )
}

fun convertWorksToCarouselModels(worksList: List<Works>): ImmutableList<CarouselModel> {
    val carouselModels = arrayListOf<CarouselModel>()

    for (works in worksList) {
        val model = works.anime ?: works.manga
        val type = if (works.anime == null) SearchType.Manga else SearchType.Anime

        if (model != null) {
            carouselModels.add(
                CarouselModel(
                    id = model.id,
                    englishName = model.name,
                    contentType = type,
                    russianName = model.russian,
                    image = model.image?.original,
                    url = model.url
                )
            )
        }
    }

    return carouselModels.toPersistentList()
}