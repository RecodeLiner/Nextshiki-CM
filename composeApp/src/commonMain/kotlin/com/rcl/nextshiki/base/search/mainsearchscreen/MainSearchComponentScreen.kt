package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State.*
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.rcl.mr.MR.strings.filter_genres
import com.rcl.mr.MR.strings.search_example
import com.rcl.mr.MR.strings.search_filter
import com.rcl.nextshiki.elements.SearchCard
import com.rcl.nextshiki.elements.getNotSelectedCardColor
import com.rcl.nextshiki.elements.getSelectedCardColor
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch

private val ToggleableState.updateState: ToggleableState
    get() {
        return when (this) {
            ToggleableState.On -> ToggleableState.Off
            ToggleableState.Off -> ToggleableState.Indeterminate
            ToggleableState.Indeterminate -> ToggleableState.On
        }
    }

@Composable
fun MainSearchComponentScreen(component: MainSearchComponent) {
    val isReachedEnd by component.isEndOfListReached.subscribeAsState()
    val coroutineScope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme
    val text by component.text.subscribeAsState()
    val sheetState = rememberFlexibleBottomSheetState()
    val searchList = component.searchedList
    val genreList = component.genresList.toMutableStateList()
    val currentType by component.currentType.subscribeAsState()
    val verticalScrollState = rememberLazyStaggeredGridState()

    LaunchedEffect(null) {
        sheetState.hide()
    }

    LaunchedEffect(verticalScrollState.isScrollingToEnd()) {
        if (verticalScrollState.isScrollingToEnd() && !isReachedEnd && searchList.size >40) {
            component.isEndOfListReached.update { true }
            component.updatePageList()
        }
    }

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        OutlinedTextField(
            placeholder = { Text(text = search_example.getLocalizableString()) },
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { value ->
                component.onTextChanged(value)
                if (text.endsWith("\n")) {
                    component.onTextChanged(value.dropLast(1))
                    component.clearList()
                    component.searchObject(text)
                }
            }
        )
        val genreRowState = rememberLazyListState()
        LazyRow(
            state = genreRowState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .draggable(
                    orientation = Horizontal,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            genreRowState.scrollBy(-delta)
                        }
                    },
                ),
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .noRippleClickable {
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                    colors = getSelectedCardColor(colorScheme)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune, contentDescription = "Filter icon",
                            modifier = Modifier.padding(horizontal = 10.dp),
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            text = search_filter.getLocalizableString()
                        )
                    }
                }
            }
            items(component.typeList, key = { item -> item.ordinal }) { type ->
                val selected = MutableValue(currentType == type)
                Card(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .noRippleClickable {
                            component.updateType(type)
                            component.clearList()
                            component.searchObject(text)
                            coroutineScope.launch {
                                verticalScrollState.scrollToItem(0)
                            }
                        },
                    colors = if (selected.value) getSelectedCardColor(colorScheme) else getNotSelectedCardColor(
                        colorScheme
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 20.dp),
                        text = type.stringResource.getLocalizableString()
                    )
                }
            }
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier.draggable(
                orientation = Vertical,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        verticalScrollState.scrollBy(-delta)
                    }
                },
            ),
            columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
            state = verticalScrollState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = (8.dp),
        ) {
            items(searchList, key = { item -> item.id }) { listItem ->
                val url = getValidImageUrl(listItem.image)
                if (url != null) {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(url)
                            .size(Size.ORIGINAL)
                            .build()
                    )
                    when (painter.state) {
                        is Success -> {
                            SearchCard(
                                modifier = Modifier
                                    .noRippleClickable {
                                        component.navigateToSearchedObject(
                                            id = listItem.id.toString(),
                                            contentType = component.currentType.value
                                        )
                                    },
                                painter = painter,
                                name = when (Locale.current.language) {
                                    "ru" -> listItem.russian!!
                                    else -> listItem.english!!
                                }
                            )
                        }

                        is Empty -> {
                            Card(modifier = Modifier.aspectRatio(1f)) {
                                Text("State is empty")
                            }
                        }

                        is Error -> {
                            Card(modifier = Modifier.aspectRatio(1f)) {
                                Text("State is error - ${(painter.state as Error).result}")
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
        }
        if (sheetState.isVisible) {
            Box(modifier = Modifier.weight(1f).align(Alignment.CenterHorizontally)) {
                FlexibleBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    }
                ) {
                    Text(
                        text = "${filter_genres.getLocalizableString()}:",
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                    )
                    LazyVerticalStaggeredGrid(
                        state = rememberLazyStaggeredGridState(),
                        columns = StaggeredGridCells.Adaptive(minSize = 150.dp)
                    ) {
                        itemsIndexed(genreList, key = { _, item -> item.obj.id!! }) { index, genre ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = when (Locale.current.language) {
                                        "ru" -> genre.obj.russian!!
                                        else -> genre.obj.name!!
                                    }
                                )
                                TriStateCheckbox(
                                    state = genre.state,
                                    onClick = {
                                        genreList[index] = genre.copy(state = genre.state.updateState)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun LazyStaggeredGridState.isScrollingToEnd(buffer: Int = 5): Boolean {
    val layoutInfo = this.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val lastItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

    return totalItems - lastItemIndex <= buffer
}