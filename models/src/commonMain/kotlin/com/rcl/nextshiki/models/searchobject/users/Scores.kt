package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scores(
    @SerialName("anime") val anime: List<ContentScore> = emptyList(),
    @SerialName("manga") val manga: List<ContentScore> = emptyList()
)
