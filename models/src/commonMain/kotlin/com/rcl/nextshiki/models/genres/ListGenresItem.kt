package com.rcl.nextshiki.models.genres

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListGenresItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("entry_type") val entryType: String? = null
)
