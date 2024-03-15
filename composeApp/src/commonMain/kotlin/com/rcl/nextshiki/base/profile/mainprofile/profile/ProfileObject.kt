package com.rcl.nextshiki.base.profile.mainprofile.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.rcl.nextshiki.models.universal.Image
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProfileObject(data: UserObject) {
    val size = calculateWindowSizeClass().widthSizeClass
    when (size) {
        Compact -> {
            mobileUI(data)
        }

        else -> {
            desktopUI(data)
        }
    }
}

@Composable
fun mobileUI(data: UserObject) {
    LazyColumn {
        item {
            profileIcon(data.image)
        }
    }
}

@Composable
fun desktopUI(data: UserObject) {
    Row {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                profileIcon(data.image)
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            item { Text("check") }
        }
    }
}

@Composable
fun profileIcon(image: Image?) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(image?.x160)
            .size(Size.ORIGINAL)
            .build()
    )
    val loader = rememberPainterLoader()
    val paletteState = rememberDominantColorState(loader = loader)
    LaunchedEffect(painter.state) {
        if (painter.state is AsyncImagePainter.State.Success) {
            paletteState.updateFrom(painter)
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .width(this.maxWidth / 2)
                .aspectRatio(1f)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(
                containerColor = paletteState.color
            )
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Success -> {
                    Image(
                        modifier = Modifier.fillMaxSize().clip(shape = RoundedCornerShape(200.dp)),
                        painter = painter,
                        contentDescription = "Profile Image"
                    )
                }

                is AsyncImagePainter.State.Empty -> {
                    Napier.i("empty - $image")
                }

                is AsyncImagePainter.State.Error -> {
                    Napier.i("Error - $image")
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}