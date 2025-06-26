package com.rcl.nextshiki.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumDto(
    @SerialName("id") val id: Int?,
    @SerialName("position") val position: Int?,
    @SerialName("name") val name: String?,
    @SerialName("permalink") val permalink: String?,
    @SerialName("url") val url: String?
)