package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentScore(
    @SerialName("name") val name: String? = null,
    @SerialName("value") val value: Int?    = null
)
