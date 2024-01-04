package com.rcl.nextshiki.base.search.mainsearchscreen

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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType.*
import com.rcl.nextshiki.elements.SearchCard
import com.rcl.nextshiki.elements.getNotSelectedCardColor
import com.rcl.nextshiki.elements.getSelectedCardColor
import com.rcl.nextshiki.noRippleClickable
import com.rcl.nextshiki.strings.MainResStrings
import com.rcl.nextshiki.strings.MainResStrings.search_anime
import com.rcl.nextshiki.strings.MainResStrings.search_filter
import com.rcl.nextshiki.strings.MainResStrings.search_manga
import com.rcl.nextshiki.strings.MainResStrings.search_people
import com.rcl.nextshiki.strings.MainResStrings.search_ranobe
import com.rcl.nextshiki.strings.MainResStrings.search_users
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch

private val SearchType.getName: String
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
    var showFilter by remember { mutableStateOf(false) }
    val sheetState = rememberFlexibleBottomSheetState()
    val searchList = component.searchedList
    val genreList = component.genresList.toMutableStateList()
    val currentType by component.currentType.subscribeAsState()

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        OutlinedTextField(
            placeholder = { Text(text = MainResStrings.search_example) },
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { value ->
                component.onTextChanged(value)
                if (text.endsWith("\n")) {
                    component.onTextChanged(value.dropLast(1))
                    component.searchObject(text)
                }
            }
        )
        val genreRowState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .draggable(
                    orientation = Vertical,
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
                            showFilter = true
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
                            text = search_filter
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
                        },
                    colors = if (selected.value) getSelectedCardColor(colorScheme) else getNotSelectedCardColor(
                        colorScheme
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 20.dp),
                        text = type.getName
                    )
                }
            }
        }
        val verticalScrollState = rememberLazyStaggeredGridState()
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
            state = verticalScrollState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
        ) {
            items(searchList) { listItem ->
                SearchCard(
                    modifier = Modifier
                        .noRippleClickable {

                        },
                    content = listItem
                )
            }
        }
        if (showFilter) {
            FlexibleBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showFilter = false
                }
            ) {
                Text(
                    text = "${MainResStrings.filter_genres}:",
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