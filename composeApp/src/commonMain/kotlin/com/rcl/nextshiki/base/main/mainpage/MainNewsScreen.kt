package com.rcl.nextshiki.base.main.mainpage

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.rcl.mr.SharedRes.strings.main_calendar
import com.rcl.mr.SharedRes.strings.main_news
import com.rcl.nextshiki.base.main.mainpage.subelements.CalendarCard
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.base.main.mainpage.subelements.TopicCard
import com.rcl.nextshiki.elements.extractLink
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNewsComponentScreen(
    newsComponent: MainNewsComponent
) {
    val calendarList = newsComponent.cardList
    val newsList = newsComponent.topicsList

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
            CardRow(
                calendarList = calendarList.toPersistentList(),
                navigate = newsComponent::navigateToCard
            )
        }
        item("newsColumnTitle", span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = main_news.getLocalizableString(),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        items(newsList, key = { topic -> topic.id ?: "Unexpected" }) { topic ->
            TopicCard(
                topic = topic,
                link = extractLink(topic.htmlFooter),
                navigate = newsComponent::navigateToNews
            )
        }
    }
}

@Composable
private fun CardRow(
    calendarList: ImmutableList<CardElement>,
    navigate: (Int) -> Unit
) {
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
            CardCarousel(card = card, navigate = navigate)
        }
    }
}

@Composable
private fun CardCarousel(card: CardElement, navigate: (Int) -> Unit) =
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(card.imageLink)
                    .size(Size.ORIGINAL)
                    .build()
            )
            val painterState by painter.state.collectAsState()
            when (painterState) {
                is Success -> {
                    getLangRes(
                        english = card.name,
                        russian = card.russian
                    )?.let { name ->
                        painterState.painter?.let {
                            CalendarCard(
                                onClick = { navigate(card.id) },
                                name = name,
                                painter = it,
                                time = card.nextEpisodeAt
                            )
                        }
                    }
                }

                is Empty -> {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Empty in MainCard + ${card.imageLink}"
                    )
                }

                is Error -> {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Error in MainCard + ${card.imageLink}, state - ${(painter.state.value as Error).result}"
                    )
                }

                is Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

@Composable
private fun TopicCard(topic: HotTopics, link: String?, navigate: (HotTopics) -> Unit) =
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        val backgroundPainter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalPlatformContext.current)
                .data(link)
                .size(Size.ORIGINAL)
                .build()
        )

        val painterState by backgroundPainter.state.collectAsState()
        when (painterState) {
            is Success -> {
                val userPainter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(topic.user?.image?.x160)
                        .size(Size.ORIGINAL)
                        .build()
                )
                val userPainterState by userPainter.state.collectAsState()
                if (topic.topicTitle != null && topic.user?.nickname != null && userPainterState is Success) {
                    TopicCard(
                        topic = topic,
                        onClick = { navigate(topic) },
                        backgroundPainter = backgroundPainter,
                        userPainter = userPainter
                    )
                }
            }

            is Empty -> {
                Text(
                    text = "state is empty - ${backgroundPainter.input.value}",
                    modifier = Modifier.padding(5.dp)
                )
            }

            is Error -> {
                Text(
                    text = "state is error - ${(backgroundPainter.state.value as Error).result}",
                    modifier = Modifier.padding(5.dp)
                )
            }

            is Loading -> {
                CircularProgressIndicator()
            }
        }
    }
