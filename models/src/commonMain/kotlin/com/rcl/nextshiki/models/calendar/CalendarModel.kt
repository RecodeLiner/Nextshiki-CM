package com.rcl.nextshiki.models.calendar

import com.rcl.nextshiki.models.searchobject.SearchListItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarModel(
    @SerialName("next_episode") val nextEpisode: Int? = null,
    @SerialName("next_episode_at") val nextEpisodeAt: String? = null,
    @SerialName("duration") val duration: Int? = null,
    @SerialName("anime") val anime: SearchListItem? = SearchListItem()
)
