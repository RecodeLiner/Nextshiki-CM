package com.rcl.nextshiki.models.topics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forums (
    @SerialName("id") val id: Int? = null,
    @SerialName("position") val position: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("permalink") val permalink: String? = null,
    @SerialName("url") val url: String? = null
)
