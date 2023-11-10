package com.rcl.nextshiki.models.franchise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FranchiseModel (
    @SerialName("links") val links: List<Links> = arrayListOf(),
    @SerialName("nodes") val nodes: List<Nodes> = arrayListOf(),
    @SerialName("current_id") val currentId: Int? = null
)
