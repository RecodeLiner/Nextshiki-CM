package com.rcl.nextshiki.data.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class UserDto(
    @SerialName("id") val id: Int,
    @SerialName("nickname") val nickname: String?,
    @SerialName("avatar") val avatar: String?,
    @SerialName("image") val image: UserImageDto?,
    @Contextual @SerialName("last_online_at") val lastOnlineAt: String?,
    @SerialName("url") val url: String?
)