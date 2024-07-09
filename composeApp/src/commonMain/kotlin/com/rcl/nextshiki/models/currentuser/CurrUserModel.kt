package com.rcl.nextshiki.models.currentuser

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrUserModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("avatar") val avatar: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("last_online_at") val lastOnlineAt: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("sex") val sex: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("birth_on") val birthOn: String? = null,
    @SerialName("full_years") val fullYears: String? = null,
    @SerialName("locale") val locale: String? = null
)
