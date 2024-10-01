package com.rcl.nextshiki.models.franchise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    @SerialName("id") val id: Int? = null,
    @SerialName("source_id") val sourceId: Int? = null,
    @SerialName("target_id") val targetId: Int? = null,
    @SerialName("source") val source: Int? = null,
    @SerialName("target") val target: Int? = null,
    @SerialName("weight") val weight: Int? = null,
    @SerialName("relation") val relation: String? = null
)
