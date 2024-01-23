package com.rcl.nextshiki.base.main

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.elements.CalendarCard

var text by remember{ mutableStateOf("TempValue") }

@Composable
fun MainComponentScreen(component: MainComponent) {
    val card by component.cardElement.subscribeAsState()

    if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
        CalendarCard(name = card.name, link = card.imageLink, time = card.nextEpisodeAt)
    }
    Text("current is $text")
}