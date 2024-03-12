package com.rcl.nextshiki.models.searchobject.users

import com.rcl.nextshiki.models.universal.ContentInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Statuses(
    @SerialName("anime") val anime: List<ContentInfo> = arrayListOf(),
    @SerialName("manga") val manga: List<ContentInfo> = arrayListOf()
)
