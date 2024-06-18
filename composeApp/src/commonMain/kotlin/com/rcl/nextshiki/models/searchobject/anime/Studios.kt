package com.rcl.nextshiki.models.searchobject.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Studios(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("filtered_name") val filteredName: String? = null,
    @SerialName("real") val real: Boolean? = null,
    @SerialName("image") val image: String? = null
)
