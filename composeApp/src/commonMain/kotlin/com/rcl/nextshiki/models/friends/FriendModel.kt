package com.rcl.nextshiki.models.friends

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendModel (
    @SerialName("notice") val notice: String? = null,
    @SerialName("errors") val errors: List<String> = listOf()
)
