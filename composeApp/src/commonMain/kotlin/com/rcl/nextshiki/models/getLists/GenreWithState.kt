package com.rcl.nextshiki.models.getLists

import androidx.compose.ui.state.ToggleableState

data class GenreWithState(
    val obj: ListGenresItem,
    val state: ToggleableState = ToggleableState.Off
)
