package com.rcl.nextshiki.models.universal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentInfo(
    @SerialName("id") val id: Int? = null,
    @SerialName("grouped_id") val groupedId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("type") val contentInfoType: String? = null
)
