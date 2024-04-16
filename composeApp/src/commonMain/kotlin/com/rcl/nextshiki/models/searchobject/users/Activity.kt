package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    @SerialName("name") val name: List<Int> = listOf(),
    @SerialName("value") val value: Int? = null
)
