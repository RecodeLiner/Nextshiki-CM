package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchCardModel(
    @SerialName("image") val image: Image = Image(),
    @SerialName("id") val id: Int,
    @SerialName("russian") val russian: String? = null,
    @SerialName("english") val english: String? = null,
)
