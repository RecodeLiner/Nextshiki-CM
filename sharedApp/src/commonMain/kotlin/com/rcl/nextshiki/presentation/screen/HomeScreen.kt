package com.rcl.nextshiki.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rcl.nextshiki.components.HomeComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreen(component: HomeComponent) {
    val state by component.store.stateFlow.collectAsState()
    val list = state.calendarEntries

    val listState = rememberLazyListState()
    LazyColumn(
        state = listState
    ) {
        items(list) { calendarItem ->
            calendarItem.anime?.name?.let { Text(it) }
        }
    }
}