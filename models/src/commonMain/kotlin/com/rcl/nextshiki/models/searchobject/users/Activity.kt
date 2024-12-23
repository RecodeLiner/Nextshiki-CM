package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    @SerialName("name") val name: List<Int> = emptyList(),
    @SerialName("value") val value: Int? = null
)
