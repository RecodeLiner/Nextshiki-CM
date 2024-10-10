package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharAnimes(
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") val score: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("episodes_aired") val episodesAired: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("roles") val roles: List<String> = emptyList(),
    @SerialName("role") val role: String? = null
) : CommonSearchInterface {
    override val searchType: SearchType
        get() = SearchType.Characters
}