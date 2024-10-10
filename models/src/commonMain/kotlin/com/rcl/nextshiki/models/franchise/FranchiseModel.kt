package com.rcl.nextshiki.models.franchise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseModel(
    @SerialName("links") val links: List<Links> = emptyList(),
    @SerialName("nodes") val nodes: List<Nodes> = emptyList(),
    @SerialName("current_id") val currentId: Int? = null
)
