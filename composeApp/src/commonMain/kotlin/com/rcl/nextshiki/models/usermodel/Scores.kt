package com.rcl.nextshiki.models.usermodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scores(
    @SerialName("anime") val anime: List<ContentScore> = arrayListOf(),
    @SerialName("manga") val manga: List<ContentScore> = arrayListOf()
)