package com.rcl.nextshiki.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkedAnimeDto(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("russian") val russian: String?,
    @SerialName("image") val image: ImageDto?,
    @SerialName("url") val url: String,
    @SerialName("kind") val kind: String?,
    @SerialName("score") val score: String?,
    @SerialName("status") val status: String?,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("episodes_aired") val episodesAired: Int?,
    @SerialName("aired_on") val airedOn: String?,
    @SerialName("released_on") val releasedOn: String?
)