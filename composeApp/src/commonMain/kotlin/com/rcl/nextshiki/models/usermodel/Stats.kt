package com.rcl.nextshiki.models.usermodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats (
    @SerialName("statuses") val statuses: Statuses? = Statuses(),
    @SerialName("full_statuses") val fullStatuses: Statuses? = Statuses(),
    @SerialName("scores") val scores: Scores? = Scores(),
    @SerialName("types") val types: Scores? = Scores(),
    @SerialName("ratings") val ratings: Scores? = Scores(),
    @SerialName("has_anime?") val hasAnime: Boolean? = null,
    @SerialName("has_manga?") val hasManga: Boolean? = null,
    @SerialName("genres") val genres: List<String?> = arrayListOf(),
    @SerialName("studios") val studios: List<String?> = arrayListOf(),
    @SerialName("publishers") val publishers: List<String?> = arrayListOf(),
    @SerialName("activity") val activity: ActivityList? = ActivityList(arrayListOf()),
)