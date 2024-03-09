package com.rcl.nextshiki.base.main.mainpage

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State.*
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.elements.TopicCard
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@Composable
fun MainNewsComponentScreen(component: MainNewsComponent) {
    val calendarList = component.cardList
    val newsList = component.topicsList
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        val rowState = rememberLazyListState()
        LazyRow(
            state = rowState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            rowState.scrollBy(-delta)
                        }
                    },
                ),
        ) {
            items(calendarList) { card ->
                Card (modifier = Modifier.fillMaxHeight().width(500.dp)) {
                    if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalPlatformContext.current)
                                .data(card.imageLink)
                                .size(Size.ORIGINAL)
                                .build()
                        )
                        when (painter.state) {
                            is Success -> {
                                CalendarCard(name = card.name, painter = painter, time = card.nextEpisodeAt)
                            }
                            is Empty -> {

                            }

                            is Error -> {

                            }

                            is Loading -> {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
            items(newsList) { topic ->
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(topic.htmlFooter?.let { component.extractLink(it) })
                        .size(Size.ORIGINAL)
                        .build()
                )

                when (painter.state) {
                    is Success -> {
                        if (topic.topicTitle != null) {
                            if (topic.user != null) {
                                TopicCard(
                                    onClick = { Napier.i("success: ${topic.id}") },
                                    painter = painter,
                                    title = topic.topicTitle,
                                    user = topic.user
                                )
                            }
                        }
                    }
                    is Empty -> {

                    }
                    is Error -> {

                    }
                    is Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}