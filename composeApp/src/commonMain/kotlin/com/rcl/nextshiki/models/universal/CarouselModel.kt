package com.rcl.nextshiki.models.universal

import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarouselModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("contentType") val contentType: SearchType,
    @SerialName("englishName") val englishName: ImmutableList<String?> = persistentListOf(),
    @SerialName("russianName") val russianName: ImmutableList<String?> = persistentListOf(),
    @SerialName("image") val image: String? = null,
    @SerialName("url") val url: String? = null
)
