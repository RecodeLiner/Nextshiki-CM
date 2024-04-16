package com.rcl.nextshiki.models.genres

import androidx.compose.ui.state.ToggleableState

data class GenreWithState(
    val obj: ListGenresItem,
    val state: ToggleableState
)
