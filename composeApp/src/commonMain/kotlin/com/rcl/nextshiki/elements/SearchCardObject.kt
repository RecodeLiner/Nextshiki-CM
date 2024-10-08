package com.rcl.nextshiki.elements

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    name: String,
    id: Int
) {
    withLocalSharedTransition{
        Card(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Column {
                Image(
                    painter = painter,
                    contentDescription = "Search card preview image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(15.dp))
                        .sharedBounds(
                            rememberSharedContentState("searched_card_${id}_image"),
                            LocalAnimatedVisibilityScope.current
                        )
                )
                Text(
                    text = name,
                    softWrap = true,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(12.dp)
                        .sharedBounds(
                            rememberSharedContentState("searched_card_${id}_name"),
                            LocalAnimatedVisibilityScope.current
                        )
                )
            }
        }
    }
}
