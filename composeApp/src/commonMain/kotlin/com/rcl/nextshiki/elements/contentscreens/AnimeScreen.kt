package com.rcl.nextshiki.elements.contentscreens

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AnimeScreen(data: AnimeObject) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        Compact -> {
            mobile(data)
        }

        else -> {
            desktop(data)
        }
    }
}

@Composable
fun mobile(data: AnimeObject) {
    LazyColumn {
        items(1000){
            Text("this is $it")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun desktop(data: AnimeObject) {
    FlowColumn() {
        for (i in 1 .. 100) {
            Text("This FC is $i")
        }
    }
}