package com.rcl.nextshiki.models.moe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoLinkModel(
    @SerialName("ok") val ok: Boolean? = null,
    @SerialName("cache") val cache: Boolean? = null,
    @SerialName("result") val result: List<ResultModel> = listOf()
)