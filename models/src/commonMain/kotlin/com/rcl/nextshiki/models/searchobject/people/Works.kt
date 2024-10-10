package com.rcl.nextshiki.models.searchobject.people

import com.rcl.nextshiki.models.history.HistoryModelEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class Works(
    @SerialName("anime") val anime: HistoryModelEntity? = HistoryModelEntity(),
    @SerialName("manga") val manga: HistoryModelEntity? = HistoryModelEntity(),
    @SerialName("role") val role: String? = null
)
