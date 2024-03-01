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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.elements.TopicCard
import io.github.aakira.napier.Napier
import io.kamel.core.isSuccess
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

@Composable
fun MainNewsComponentScreen(component: MainNewsComponent) {
    val calendarList = component.cardList
    val newsList = component.topicsList
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        val rowState = rememberLazyListState()
        LazyRow(
            state = rowState, modifier = Modifier
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
                if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
                    CalendarCard(name = card.name, link = card.imageLink, time = card.nextEpisodeAt)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
            items(newsList) { topic ->
                val painter =
                    topic.htmlFooter?.let { it -> component.extractLink(it)?.let { asyncPainterResource(data = it) } }
                if (painter?.isSuccess == true) {
                    if (topic.topicTitle != null) {
                        if (topic.user != null) {
                            TopicCard(
                                onClick = { Napier.i("success: ${topic.id}") },
                                image = painter,
                                title = topic.topicTitle,
                                user = topic.user
                            )
                        }
                    }
                }
            }
        }
    }
}