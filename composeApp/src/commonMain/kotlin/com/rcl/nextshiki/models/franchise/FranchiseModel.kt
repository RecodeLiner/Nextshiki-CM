package com.rcl.nextshiki.models.franchise

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class FranchiseModel(
    @SerialName("links") val links: List<Links> = listOf(),
    @SerialName("nodes") val nodes: List<Nodes> = listOf(),
    @SerialName("current_id") val currentId: Int? = null
)
