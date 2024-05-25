package com.rcl.nextshiki.models.searchobject.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Videos(
    @SerialName("id") val id: Int? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("player_url") val playerUrl: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("hosting") val hosting: String? = null
)
