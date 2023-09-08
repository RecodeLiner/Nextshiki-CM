package com.rcl.nextshiki.models.history

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryModelEntity (
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("russian") var russian: String? = null,
    @SerialName("image") var image: Image? = Image(),
    @SerialName("url") var url: String? = null,
    @SerialName("kind") var kind: String? = null,
    @SerialName("score") var score: String? = null,
    @SerialName("status") var status: String? = null,
    @SerialName("episodes") var episodes: Int? = null,
    @SerialName("episodes_aired") var episodesAired: Int? = null,
    @SerialName("aired_on") var airedOn: String? = null,
    @SerialName("released_on") var releasedOn: String? = null
)
