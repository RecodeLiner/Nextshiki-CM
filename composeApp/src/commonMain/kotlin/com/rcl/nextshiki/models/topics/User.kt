package com.rcl.nextshiki.models.topics

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("avatar") val avatar: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("last_online_at") val lastOnlineAt: String? = null,
    @SerialName("url") val url: String? = null
)