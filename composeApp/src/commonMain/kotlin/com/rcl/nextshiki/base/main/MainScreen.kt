package com.rcl.nextshiki.base.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.elements.CalendarCard

@Composable
fun MainComponentScreen(component: MainComponent) {
    val card by component.cardElement.subscribeAsState()

    if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
        CalendarCard(name = card.name, link = card.imageLink, time = card.nextEpisodeAt)
    }
}