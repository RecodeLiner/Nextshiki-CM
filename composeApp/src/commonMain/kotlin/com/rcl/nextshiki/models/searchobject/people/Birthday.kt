package com.rcl.nextshiki.models.searchobject.people

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Birthday (
    @SerialName("day") val day: Int? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("month") val month: Int? = null
)
