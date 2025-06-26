package com.rcl.nextshiki.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class CalendarEntry(
    val nextEpisode: Int?,
    val nextEpisodeAt: Instant?,
    val duration: Int?,
    val anime: Anime?
)