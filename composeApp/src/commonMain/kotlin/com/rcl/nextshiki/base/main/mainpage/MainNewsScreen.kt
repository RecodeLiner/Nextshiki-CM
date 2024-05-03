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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State.*
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.nextshiki.base.main.mainpage.subelements.CalendarCard
import com.rcl.nextshiki.base.main.mainpage.subelements.TopicCard
import com.rcl.nextshiki.locale.Locale.getComposeLocalizedText
import kotlinx.coroutines.launch

@Composable
fun MainNewsComponentScreen(component: MainNewsComponent) {
    val calendarList = component.cardList
    val newsList = component.topicsList
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        val rowState = rememberLazyListState()
        Text(
            text = getComposeLocalizedText().main_calendar
        )
        LazyRow(
            state = rowState,
            modifier = Modifier
                .padding(top = 10.dp)
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(calendarList) { card ->
                Card(modifier = Modifier.fillMaxHeight().aspectRatio(1f)) {
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
                                CalendarCard(
                                    name = card.name,
                                    painter = painter,
                                    time = card.nextEpisodeAt
                                )
                            }

                            is Empty -> {
                                Text("Empty in MainCard + ${card.imageLink}")
                            }

                            is Error -> {
                                Text("Error in MainCard + ${card.imageLink}, state - ${(painter.state as Error).result}")
                            }

                            is Loading -> {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = getComposeLocalizedText().main_news,
            modifier = Modifier.padding(top = 10.dp)
        )
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 25.dp),
            columns = GridCells.Adaptive(250.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(newsList, key = { topic -> topic.id?: "Unexpected" } ) { topic ->
                Card(modifier = Modifier.aspectRatio(1f)) {
                    val backgroundPainter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(component.extractLink(topic.htmlFooter))
                            .size(Size.ORIGINAL)
                            .build()
                    )

                    when (backgroundPainter.state) {
                        is Success -> {
                            val userPainter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalPlatformContext.current)
                                    .data(topic.user?.image?.x160)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )
                            if (topic.topicTitle != null && topic.user?.nickname != null && userPainter.state is Success) {
                                TopicCard(
                                    onClick = { component.navigateToNews(topic) },
                                    backgroundPainter = backgroundPainter,
                                    title = topic.topicTitle,
                                    userNickname = topic.user.nickname,
                                    userPainter = userPainter
                                )
                            }
                        }

                        is Empty -> {
                            Text("state is empty")
                        }

                        is Error -> {
                            Text("state is error - ${(backgroundPainter.state as Error).result}")
                        }

                        is Loading -> {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}