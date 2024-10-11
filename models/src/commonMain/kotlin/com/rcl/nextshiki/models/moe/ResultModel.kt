package com.rcl.nextshiki.models.moe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultModel(
    @SerialName("link") val link: String? = null,
    @SerialName("quality") val quality: String? = null,
    @SerialName("translation") val translation: TranslationModel? = TranslationModel(),
    @SerialName("title") val title: String? = null
)
