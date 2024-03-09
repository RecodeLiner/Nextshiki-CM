package com.rcl.nextshiki.base.search.mainsearchscreen

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.rcl.moko.MR.strings.filter_genres
import com.rcl.moko.MR.strings.search_anime
import com.rcl.moko.MR.strings.search_example
import com.rcl.moko.MR.strings.search_filter
import com.rcl.moko.MR.strings.search_manga
import com.rcl.moko.MR.strings.search_people
import com.rcl.moko.MR.strings.search_ranobe
import com.rcl.moko.MR.strings.search_users
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType.*
import com.rcl.nextshiki.elements.SearchCard
import com.rcl.nextshiki.elements.getNotSelectedCardColor
import com.rcl.nextshiki.elements.getSelectedCardColor
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.models.universal.Image
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

private val SearchType.getName: StringResource
    get() {
        return when (this) {
            Anime -> search_anime
            Manga -> search_manga
            Ranobe -> search_ranobe
            People -> search_people
            Users -> search_users
        }
    }
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

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        OutlinedTextField(
            placeholder = { Text(text = stringResource(search_example)) },
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
                            text = stringResource(search_filter)
                        )
                    }
                }
            }
            items(component.typeList) { type ->
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
                        text = stringResource(type.getName)
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
            itemsIndexed(searchList) { num, listItem ->
                if (num == searchList.lastIndex) {
                    component.updatePageList()
                }
                val url = getValidImageUrl(listItem.image)
                if (url != null) {
                    val painter = rememberAsyncImagePainter(url)
                    if(painter.state is AsyncImagePainter.State.Success) {
                        SearchCard(
                            modifier = Modifier
                                .noRippleClickable {
                                    listItem.id?.let { id ->
                                        component.navigateToSearchedObject(
                                            id = id,
                                            type = component.currentType.value
                                        )
                                    }
                                },
                            painter = painter,
                            name = when (Locale.current.language) {
                                "ru" -> listItem.russian!!
                                else -> listItem.english!!
                            }
                        )
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
                        text = "${stringResource(filter_genres)}:",
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                    )
                    LazyVerticalStaggeredGrid(
                        state = rememberLazyStaggeredGridState(),
                        columns = StaggeredGridCells.Adaptive(minSize = 150.dp)
                    ) {
                        itemsIndexed(genreList) { index, genre ->
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

fun getValidImageUrl(image: Image): String? {
    return when {
        image.original != null -> {
            if (image.original.contains("https://") || image.original.contains("http://")) {
                image.original
            } else {
                BuildConfig.DOMAIN + image.original
            }
        }

        image.x160 != null -> {
            if (image.x160.contains("https://") || image.x160.contains("http://")) {
                image.x160
            } else {
                BuildConfig.DOMAIN + image.x160
            }
        }

        else -> {
            null
        }
    }
}