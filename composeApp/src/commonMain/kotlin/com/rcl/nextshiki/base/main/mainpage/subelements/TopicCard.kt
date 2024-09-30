package com.rcl.nextshiki.base.main.mainpage.subelements

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.elements.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.elements.withLocalSharedTransition
import com.rcl.nextshiki.models.topics.HotTopics

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
                contentScale = ContentScale.Crop,
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
                        contentScale = ContentScale.Crop,
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
