package com.rcl.nextshiki.models.searchobject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Publishers(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null
)
