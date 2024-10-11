package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchCardModel(
    @SerialName("image") override val image: Image = Image(),
    @SerialName("id") override val id: Int,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("english") val english: String? = null,
    override val searchType: SearchType,
) : CommonSearchInterface {
    override val name: String?
        get() = english
    override val url: String?
        get() = null
}
