package com.rcl.nextshiki.compose.screens

import Nextshiki.resources.BuildConfig
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Empty
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.SharedRes.strings.search_example
import com.rcl.nextshiki.SharedRes.strings.search_filter
import com.rcl.nextshiki.SharedRes.strings.text_empty
import com.rcl.nextshiki.components.searchcomponent.mainsearchscreen.MainSearchComponent
import com.rcl.nextshiki.components.searchcomponent.mainsearchscreen.toSearchCard
import com.rcl.nextshiki.compose.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.compose.getCardColors
import com.rcl.nextshiki.compose.getLangRes
import com.rcl.nextshiki.compose.noRippleClickable
import com.rcl.nextshiki.compose.withLocalSharedTransition
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.utils.getValidImageUrl
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun MainSearchScreen(mainSearchComponent: MainSearchComponent) {
    val vm = mainSearchComponent.vm
    val currentLang by vm.languageRepo.localeVar.subscribeAsState()
    val typeList = vm.typeList
    val coroutineScope = rememberCoroutineScope()
    val text by vm.text.subscribeAsState()
    //val genreList = vm.genresList.subscribeAsState()
    val currentType by vm.currentType.collectAsState()
    val verticalScrollState = rememberLazyStaggeredGridState()

    // Получаем поток данных пагинации
    val searchList = vm.pagingDataFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        CustomSearchBar(
            text = text,
            onValueChange = vm::onSearchBarValueChange
        )
        TypeRow(
            changeStateSheet = {
                //mainSearchComponent.sheetState.show()
            },
            typeList = typeList,
            currentType = currentType,
            onClick = { type ->
                vm.currentType.update { type }
                coroutineScope.launch { verticalScrollState.scrollToItem(0) }
            }
        )
        SearchResult(
            currentType = currentType,
            navigateToSearchedObject = mainSearchComponent::navigateToSearchedObject,
            searchList = searchList,
            verticalScrollState = verticalScrollState,
            langCode = currentLang,
        )
    }
}

@Composable
private fun SearchResult(
    langCode: String,
    verticalScrollState: LazyStaggeredGridState,
    searchList: LazyPagingItems<SearchListItem>,
    currentType: SearchType,
    navigateToSearchedObject: (SearchCardModel, SearchType) -> Unit,
) {
    val scope = rememberCoroutineScope()
    LazyVerticalStaggeredGrid(
        modifier = Modifier.draggable(
            orientation = Vertical,
            state = rememberDraggableState { delta ->
                scope.launch {
                    verticalScrollState.scrollBy(-delta)
                }
            },
        ),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        state = verticalScrollState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
    ) {
        items(searchList.itemCount) { index ->
            val listItem = searchList[index]
            listItem?.let {
                SearchCardLoad(
                    item = it.toSearchCard(type = currentType),
                    currentType = currentType,
                    navigateToSearchedObject = navigateToSearchedObject,
                    langCode = langCode
                )
            }
        }
        searchList.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
                is LoadState.Error -> {
                    item {
                        Text(
                            text = "Error loading data",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun SearchCardLoad(
    langCode: String,
    item: SearchCardModel,
    currentType: SearchType,
    navigateToSearchedObject: (SearchCardModel, SearchType) -> Unit
) {
    val url = getValidImageUrl(image = item.image, BuildConfig.DOMAIN)
    if (url != null) {
        val painter = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(url)
                .size(Size.ORIGINAL)
                .build()
        )
        val painterState by painter.state.collectAsState()
        when (painterState) {
            is Success -> {
                SearchCard(
                    modifier = Modifier
                        .noRippleClickable {
                            navigateToSearchedObject(
                                item,
                                currentType
                            )
                        },
                    painter = painter,
                    name = item.russian?.let {
                        item.english?.let { english ->
                            getLangRes(
                                currentCode = langCode,
                                russian = it,
                                english = english
                            )
                        }
                    }?: stringResource(text_empty),
                    id = item.id
                )
            }

            is Empty -> {
                Card(modifier = Modifier.aspectRatio(1f)) {
                    Text("State is empty")
                }
            }

            is AsyncImagePainter.State.Error -> {
                Card(modifier = Modifier.aspectRatio(1f)) {
                    Text("State is error - ${(painter.state.value as AsyncImagePainter.State.Error).result}")
                }
            }

            is Loading -> {
                Card(modifier = Modifier.aspectRatio(1f)) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
            }
        }

    }
}

@Composable
private fun CustomSearchBar(text: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        placeholder = { Text(text = stringResource(search_example)) },
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = onValueChange
    )
}

@Composable
private fun TypeRow(
    changeStateSheet: suspend () -> Unit,
    typeList: List<SearchType>,
    currentType: SearchType,
    onClick: (SearchType) -> Unit
) {
    val scope = rememberCoroutineScope()
    val genreRowState = rememberLazyListState()
    LazyRow(
        state = genreRowState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .draggable(
                orientation = Horizontal,
                state = rememberDraggableState { delta ->
                    scope.launch {
                        genreRowState.scrollBy(-delta)
                    }
                },
            ),
    ) {
        item(key = "filterCard") {
            FilterCard(changeStateSheet = changeStateSheet)
        }
        items(typeList, key = { item -> item.ordinal }) { type ->
            TypeCard(
                type = type,
                selected = currentType == type,
                onClick = { onClick(type) }
            )
        }
    }
}

@Composable
private fun FilterCard(
    changeStateSheet: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .noRippleClickable {
                coroutineScope.launch {
                    changeStateSheet()
                }
            },
        colors = getCardColors(true)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Tune, contentDescription = "Filter icon",
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = stringResource(search_filter)
            )
        }
    }
}

@Composable
private fun TypeCard(
    type: SearchType,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .noRippleClickable(onClick),
        colors = getCardColors(selected)
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 20.dp),
            text = stringResource(type.stringResource)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    name: String,
    id: Int
) {
    withLocalSharedTransition {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Column {
                Image(
                    painter = painter,
                    contentDescription = "Search card preview image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(15.dp))
                        .sharedBounds(
                            rememberSharedContentState("searched_card_${id}_image"),
                            LocalAnimatedVisibilityScope.current
                        )
                )
                Text(
                    text = name,
                    softWrap = true,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(12.dp)
                        .sharedBounds(
                            rememberSharedContentState("searched_card_${id}_name"),
                            LocalAnimatedVisibilityScope.current
                        )
                )
            }
        }
    }
}