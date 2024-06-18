package com.rcl.nextshiki.models.searchobject.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Screenshots(
    @SerialName("original") val original: String? = null,
    @SerialName("preview") val preview: String? = null
)