package com.rcl.nextshiki.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR.strings.search_anime
import com.rcl.nextshiki.MR.strings.search_example
import com.rcl.nextshiki.MR.strings.search_filter
import com.rcl.nextshiki.MR.strings.search_manga
import com.rcl.nextshiki.MR.strings.search_people
import com.rcl.nextshiki.MR.strings.search_ranobe
import com.rcl.nextshiki.MR.strings.search_users
import com.rcl.nextshiki.elements.SearchCard
import com.rcl.nextshiki.elements.SelectableRowElement
import com.rcl.nextshiki.getString
import com.rcl.nextshiki.noRippleClickable
import com.rcl.nextshiki.screens.search.searchelement.SearchElementScreen
import com.rcl.nextshiki.supper
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class SearchScreen : Screen {
    enum class ContentTypes {
        Animes,
        Mangas,
        Ranobe,
        People,
        Users
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val vm = rememberScreenModel { SearchViewModel() }
        val navigator = LocalNavigator.currentOrThrow
        var text by remember { mutableStateOf("") }
        var selected by remember { mutableStateOf("") }
        selected = getString(search_anime)

        LaunchedEffect(Unit){
            Napier.i(vm.listContent.toList().toString())
            Napier.i(vm.prepText.value)
        }

        LaunchedEffect(text) {
            vm.prepText.value = text
        }

        val coroutineScope = rememberCoroutineScope()

        val radioOptions = listOf(
            getString(search_anime),
            getString(search_manga),
            getString(search_ranobe),
            getString(search_people),
            getString(search_users)
        )

        Scaffold { paddings ->
            Box(modifier = Modifier.padding(paddings).padding(horizontal = 16.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        leadingIcon = { Icon(Default.Search, "Search icon") },
                        trailingIcon = { Icon(Default.MoreVert, "Vertical menu") },
                        value = text,
                        onValueChange = {
                            text = it
                            vm.clearList()
                            if (it.endsWith("\n")) {
                                vm.hasNext.value = true
                                vm.getContent(
                                    name = text,
                                    type = ContentTypes.entries[radioOptions.indexOf(selected)].name.supper(),
                                    page = vm.page.value
                                )
                                text = it.dropLast(1)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = Search),
                        keyboardActions = KeyboardActions(onSearch = {

                        }),
                        placeholder = { Text(getString(search_example)) }
                    )
                    //val horizontalScrollState = rememberLazyListState()
                    LazyRow(
                        //state = horizontalScrollState,
                        modifier = Modifier
                            /*.draggable(
                                orientation = Horizontal,
                                state = rememberDraggableState { delta ->
                                    coroutineScope.launch {
                                        horizontalScrollState.scrollBy(-delta)
                                    }
                                },
                            )*/
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Default.Tune,
                                        contentDescription = "Filter icon"
                                    )
                                    Text(
                                        text = getString(search_filter)
                                    )
                                }
                            }
                        }
                        items(radioOptions) { name ->
                            SelectableRowElement(
                                modifier = Modifier
                                    .noRippleClickable {
                                        if (selected != name) {
                                            vm.clearList()
                                            selected = name
                                        }
                                    },
                                selected = selected == name,
                                text = name
                            )
                        }
                    }
                    //modal bottom sheet will be with release in jetbrains compose
                    val verticalScrollState = rememberLazyStaggeredGridState()
                    LazyVerticalStaggeredGrid(
                        state = verticalScrollState,
                        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.padding(top = 16.dp)
                            .draggable(
                                orientation = Vertical,
                                state = rememberDraggableState { delta ->
                                    coroutineScope.launch {
                                        verticalScrollState.scrollBy(-delta)
                                    }
                                },
                            ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp
                    ) {
                        vm.listContent.forEach { listItem ->
                            item {
                                SearchCard(
                                    modifier = Modifier.noRippleClickable {
                                        navigator.push(
                                            SearchElementScreen(
                                                type = ContentTypes.entries[radioOptions.indexOf(selected)].name.supper(),
                                                id = listItem.id.toString()
                                            )
                                        )
                                    },
                                    content = listItem
                                )
                            }
                            if (listItem == vm.listContent.last() && vm.hasNext.value) {
                                vm.page.value++
                                vm.getContent(
                                    name = text,
                                    type = ContentTypes.entries[radioOptions.indexOf(selected)].name.supper(),
                                    page = vm.page.value
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
