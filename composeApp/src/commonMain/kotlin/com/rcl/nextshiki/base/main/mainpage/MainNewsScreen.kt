package com.rcl.nextshiki.base.main.mainpage

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import coil3.compose.AsyncImagePainter.State.Empty
import coil3.compose.AsyncImagePainter.State.Error
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.rcl.mr.MR.strings.main_calendar
import com.rcl.mr.MR.strings.main_news
import com.rcl.nextshiki.base.main.mainpage.subelements.CalendarCard
import com.rcl.nextshiki.base.main.mainpage.subelements.TopicCard
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import kotlinx.coroutines.launch

@Composable
fun MainNewsComponentScreen(component: MainNewsComponent) {
    val calendarList = component.cardList
    val newsList = component.topicsList
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(250.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item("calendarCardsRowTitle", span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = main_calendar.getLocalizableString()
            )
        }
        item("calendarCardsRow", span = { GridItemSpan(maxLineSpan) }) {
            val rowState = rememberLazyListState()
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
                items(calendarList, key = { it.id }) { card ->
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
                                    getLangRes(english = card.name, russian = card.russian)?.let { name ->
                                        CalendarCard(
                                            onClick = { component.navigateToCard(card.id) },
                                            name = name,
                                            painter = painter,
                                            time = card.nextEpisodeAt
                                        )
                                    }
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
        }
        item("newsColumnTitle", span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = main_news.getLocalizableString(),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        items(newsList, key = { topic -> topic.id ?: "Unexpected" }) { topic ->
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