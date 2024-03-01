package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.models.topics.User
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun TopicCard(onClick: () -> Unit, title: String, image: Resource<Painter>, user: User) {
    Column (modifier = Modifier.fillMaxSize().noRippleClickable { onClick.invoke() }) {
        Box {
            KamelImage(
                resource = image,
                contentDescription = "News preview pic",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight()
            )
        }

        Row {
            user.image?.x160?.let { asyncPainterResource(it) }?.let {
                KamelImage(
                    resource = it,
                    contentDescription = "News user icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(10.dp).aspectRatio(ratio = 1f)
                )
            }
            Column {
                user.nickname?.let { Text(it) }
                user.lastOnlineAt?.let { Text(it) }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            modifier = Modifier.padding(10.dp)
        )
    }
}