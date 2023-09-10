package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.generateImageLoader
import com.rcl.nextshiki.screens.main.MainViewModel
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter

@Composable
fun CalendarCardPreview(vm: MainViewModel) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(220.dp),
) {
    Text(
        vm.nearTitle.anime!!.name!!
    )
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        val painter = rememberImagePainter(BuildConfig.DOMAIN+vm.nearTitle.anime!!.image!!.original!!)
        Image(
            painter = painter,
            contentDescription = "Calendar preview image",
        )
    }
}