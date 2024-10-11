package com.rcl.nextshiki.models.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("target") val target: HistoryModelEntity? = HistoryModelEntity()
)
