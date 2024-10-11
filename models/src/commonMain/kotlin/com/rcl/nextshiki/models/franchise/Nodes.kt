package com.rcl.nextshiki.models.franchise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nodes(
    @SerialName("id") val id: Int? = null,
    @SerialName("date") val date: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("weight") val weight: Int? = null,
)
