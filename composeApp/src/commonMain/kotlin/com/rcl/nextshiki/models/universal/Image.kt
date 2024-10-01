package com.rcl.nextshiki.models.universal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("original") val original: String? = null,
    @SerialName("preview") val preview: String? = null,
    @SerialName("x160") val x160: String? = null,
    @SerialName("x148") val x148: String? = null,
    @SerialName("x80") val x80: String? = null,
    @SerialName("x64") val x64: String? = null,
    @SerialName("x48") val x48: String? = null,
    @SerialName("x32") val x32: String? = null,
    @SerialName("x16") val x16: String? = null
)
