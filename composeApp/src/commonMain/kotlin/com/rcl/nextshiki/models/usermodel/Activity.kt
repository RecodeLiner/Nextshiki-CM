package com.rcl.nextshiki.models.usermodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    @SerialName("name") val name: List<Int> = arrayListOf(),
    @SerialName("value") val value: Int? = null
)
