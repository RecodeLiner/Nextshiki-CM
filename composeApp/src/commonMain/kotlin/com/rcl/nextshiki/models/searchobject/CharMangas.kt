package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharMangas(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("url") val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") val score: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("volumes") val volumes: Int? = null,
    @SerialName("chapters") val chapters: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("roles") val roles: List<String> = listOf(),
    @SerialName("role") val role: String? = null
)
