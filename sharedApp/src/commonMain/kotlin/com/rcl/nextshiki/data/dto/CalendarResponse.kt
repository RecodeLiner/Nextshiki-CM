package com.rcl.nextshiki.data.dto

import com.rcl.nextshiki.data.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    @SerialName("next_episode") val nextEpisode: Int?,
    @SerialName("next_episode_at") val nextEpisodeAt: String?,
    @SerialName("duration") val duration: Int?,
    @SerialName("anime") val anime: AnimeDto?
)