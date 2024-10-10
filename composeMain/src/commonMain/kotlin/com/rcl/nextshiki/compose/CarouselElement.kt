package com.rcl.nextshiki.compose

import Nextshiki.resources.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.SharedRes.strings.more
import com.rcl.nextshiki.SharedRes.strings.picture_error
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.utils.getValidImageUrl
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun CommonCarouselList(
    currentCode: String,
    searchType: SearchType,
    navigateTo: (SearchCardModel, SearchType) -> Unit,
    title: StringResource,
    carouselList: List<CommonSearchInterface>,
    hasNext: Boolean
) {
    val rowState = rememberLazyListState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
            CarouselCard(currentCode,rowState, carouselList, navigateTo, hasNext, searchType)
        }
    }
}

@Composable
private fun CarouselCard(
    currentCode: String,
    rowState: LazyListState,
    carouselList: List<CommonSearchInterface>,
    navigateTo: (SearchCardModel, SearchType) -> Unit,
    hasNext: Boolean,
    searchType: SearchType
) {
    val coroutineScope = rememberCoroutineScope()
    Card {
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
                CarouselItem(
                    currentCode = currentCode,
                    carouselItem, navigateTo, searchType
                )
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
private fun CarouselItem(
    currentCode: String,
    carouselItem: CommonSearchInterface,
    navigateTo: (SearchCardModel, SearchType) -> Unit,
    searchType: SearchType
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.width(50.dp).noRippleClickable {
            if (carouselItem.id != null) {
                navigateTo(carouselItem as SearchCardModel, searchType)
            }
        }
    ) {
        carouselItem.image?.let { imageLink ->
            Box {
                getValidImageUrl(imageLink, BuildConfig.DOMAIN)?.let { CarouselIcon(url = it) }
            }
        }

        carouselItem.name?.let { english ->
            carouselItem.russian?.let { russian ->
                Text(
                    text = getLangRes(currentCode = currentCode,english = english, russian = russian),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
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
        Text(stringResource(more), maxLines = 1)
    }
}

@Composable
private fun CarouselIcon(url: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current).data(url).size(Size.ORIGINAL).build()
    )
    val painterState by painter.state.collectAsState()
    when (painterState) {
        is AsyncImagePainter.State.Error -> {
            Column {
                Icon(Icons.Default.Error, contentDescription = "error in carousel")
                Text(stringResource(picture_error))
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