package com.rcl.nextshiki.models.genres

import kotlinx.serialization.Serializable

@Serializable
data class GenreWithState(
    val obj: ListGenresItem,
    val state: ETriState
)
