package com.rcl.nextshiki.models.searchobject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesScoresStats(
    @SerialName("name") val name: String? = null,
    @SerialName("value") val value: Int? = null
)
