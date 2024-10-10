package com.rcl.nextshiki.compose.screens

import Nextshiki.resources.BuildConfig
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Empty
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.AsyncImagePainter.State.Success
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.SharedRes.strings.future_calendar
import com.rcl.nextshiki.SharedRes.strings.main_calendar
import com.rcl.nextshiki.SharedRes.strings.main_news
import com.rcl.nextshiki.SharedRes.strings.past_calendar
import com.rcl.nextshiki.components.newscomponent.mainpage.MainNewsComponent
import com.rcl.nextshiki.components.newscomponent.mainpage.subelements.CardElement
import com.rcl.nextshiki.compose.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.compose.getLangRes
import com.rcl.nextshiki.compose.noRippleClickable
import com.rcl.nextshiki.compose.withLocalSharedTransition
import com.rcl.nextshiki.models.topics.HotTopics
import com.rcl.nextshiki.utils.extractLink
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch


@Composable
fun MainNewsScreen(mainNewsComponent: MainNewsComponent) {
    val vm = mainNewsComponent.vm
    val cardList by vm.cardList.subscribeAsState()
    val topicsList by vm.topicsList.subscribeAsState()
    val currentCode by vm.languageRepo.localeVar.subscribeAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(250.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item("calendarCardsRowTitle", span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(main_calendar)
            )
        }
        item("calendarCardsRow", span = { GridItemSpan(maxLineSpan) }) {
            CardRow(
                currentCode = currentCode,
                calendarList = cardList,
                navigate = mainNewsComponent::navigateToCard,
                getCurrentTime = vm::getCurrentTime
            )
        }
        item("newsColumnTitle", span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(main_news),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        items(topicsList, key = { topic -> topic.id ?: "Unexpected" }) { topic ->
            TopicCardModel(
                topic = topic,
                link = extractLink(topic.htmlFooter, BuildConfig.DOMAIN),
                navigate = mainNewsComponent::navigateToNews
            )
        }
    }
}

@Composable
private fun CardRow(
    currentCode: String,
    calendarList: List<CardElement>,
    navigate: (Int) -> Unit,
    getCurrentTime: (String) -> Int
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
            CardCarousel(
                currentCode = currentCode,
                card = card, navigate = navigate,
                getCurrentTime = getCurrentTime
            )
        }
    }
}

@Composable
private fun CardCarousel(
    currentCode: String,
    card: CardElement,
    navigate: (Int) -> Unit,
    getCurrentTime: (String) -> Int
) =
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
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
                        currentCode = currentCode,
                        english = card.name,
                        russian = card.russian
                    ).let { name ->
                        painterState.painter?.let {
                            CalendarCard(
                                onClick = { navigate(card.id) },
                                name = name,
                                painter = it,
                                time = getCurrentTime(card.nextEpisodeAt)
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

                is AsyncImagePainter.State.Error -> {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Error in MainCard + ${card.imageLink}, state - ${(painter.state.value as AsyncImagePainter.State.Error).result}"
                    )
                }

                is Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

@Composable
private fun TopicCardModel(topic: HotTopics, link: String?, navigate: (HotTopics) -> Unit) =
    Card {
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

            is AsyncImagePainter.State.Error -> {
                Text(
                    text = "state is error - ${(backgroundPainter.state.value as AsyncImagePainter.State.Error).result}",
                    modifier = Modifier.padding(5.dp)
                )
            }

            is Loading -> {
                CircularProgressIndicator()
            }
        }
    }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Stable
fun TopicCard(
    topic: HotTopics,
    onClick: () -> Unit,
    backgroundPainter: Painter,
    userPainter: Painter
) {
    Column(
        modifier = Modifier
            .clip(CardDefaults.shape)
            .noRippleClickable(onClick)
    ) {
        withLocalSharedTransition {
            Image(
                painter = backgroundPainter,
                contentDescription = "News preview pic",
                contentScale = Crop,
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CardDefaults.shape)
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState("${topic.id} news image"),
                        animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                        clipInOverlayDuringTransition = OverlayClip(CardDefaults.shape)
                    )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = topic.topicTitle!!,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState("${topic.id} news title"),
                            LocalAnimatedVisibilityScope.current
                        )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Image(
                        painter = userPainter,
                        contentDescription = "News user image",
                        contentScale = Crop,
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp)
                            .clip(CircleShape)
                            .sharedBounds(
                                sharedContentState =
                                rememberSharedContentState("${topic.id} news source image"),
                                animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                                clipInOverlayDuringTransition = OverlayClip(CircleShape)
                            )
                    )
                    Text(
                        text = topic.user!!.nickname!!,
                        modifier = Modifier.padding(start = 5.dp)
                            .sharedBounds(
                                sharedContentState =
                                rememberSharedContentState("${topic.id} news source nickname"),
                                animatedVisibilityScope = LocalAnimatedVisibilityScope.current
                            ),
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        minLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarCard(
    name: String,
    time: Int,
    painter: Painter,
    onClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize().noRippleClickable(onClick)) {
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
            contentScale = Crop,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(100.dp)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0F to Color.Transparent,
                        1F to Color.Black
                    )
                )
        )
        Text(
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp),
            text = name,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                text = if (time < 0) {
                    stringResource(past_calendar)
                } else {
                    stringResource(future_calendar, time)
                }
            )
        }
    }
}