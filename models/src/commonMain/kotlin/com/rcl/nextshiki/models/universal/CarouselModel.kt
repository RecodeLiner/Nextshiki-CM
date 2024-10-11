package com.rcl.nextshiki.models.universal

import com.rcl.nextshiki.models.searchobject.SearchType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarouselModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("contentType") val contentType: SearchType,
    @SerialName("englishName") val englishName: List<String?> = emptyList(),
    @SerialName("russianName") val russianName: List<String?> = emptyList(),
    @SerialName("image") val image: String? = null,
    @SerialName("url") val url: String? = null
)
