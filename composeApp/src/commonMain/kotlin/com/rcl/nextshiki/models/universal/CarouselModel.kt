package com.rcl.nextshiki.models.universal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarouselModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("englishName") val englishName: String? = null,
    @SerialName("russianName") val russianName: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("url") val url: String? = null
)
