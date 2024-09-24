package com.rcl.nextshiki.base.main.mainpage.subelements

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.elements.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.elements.LocalSharedTransitionScope
import com.rcl.nextshiki.elements.noRippleClickable
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F)
            .clip(RoundedCornerShape(25.dp))
            .noRippleClickable(onClick)
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = "News preview pic",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.9f)
                )
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.BottomStart)
        ) {
            with(LocalSharedTransitionScope.current){
                Text(
                    text = topic.topicTitle!!,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState("${topic.id} news title"),
                        LocalAnimatedVisibilityScope.current
                    )
                )
            }
            Row(modifier = Modifier.padding(top = 5.dp)) {
                Image(
                    painter = userPainter,
                    contentDescription = "News user image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(25.dp).width(25.dp).clip(RoundedCornerShape(25.dp))
                )
                Text(
                    text = topic.user!!.nickname!!,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.White
                )
            }
        }
    }
}
