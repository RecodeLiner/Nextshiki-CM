package com.rcl.nextshiki.models.searchobject.characters

import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterSearchModel(
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null
) : CommonSearchInterface
