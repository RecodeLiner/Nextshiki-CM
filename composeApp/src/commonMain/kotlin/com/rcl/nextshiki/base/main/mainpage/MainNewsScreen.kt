package com.rcl.nextshiki.base.main.mainpage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.elements.CalendarCard

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainNewsComponentScreen(component: MainNewsComponent) {
    val size = calculateWindowSizeClass().widthSizeClass

    when (size) {
        Compact -> {
            mobileView(component)
        }

        else -> {
            desktopView(component)
        }
    }
}

@Composable
private fun mobileView(component: MainNewsComponent) {
    val card by component.cardElement.subscribeAsState()
    LazyColumn {
        item {
            Card(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
                    CalendarCard(name = card.name, link = card.imageLink, time = card.nextEpisodeAt)
                }
            }
        }
    }
}

@Composable
private fun desktopView(component: MainNewsComponent) {
    val card by component.cardElement.subscribeAsState()
    Row {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Card(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                    if (card.name.isNotEmpty() && card.imageLink.isNotEmpty() && card.nextEpisodeAt.isNotEmpty()) {
                        CalendarCard(name = card.name, link = card.imageLink, time = card.nextEpisodeAt)
                    }
                }
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {

            }
        }
    }
}