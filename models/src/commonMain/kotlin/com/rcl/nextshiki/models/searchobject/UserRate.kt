package com.rcl.nextshiki.models.searchobject

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRate(
    @SerialName("id") val id: Int? = null,
    @SerialName("score") val score: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("chapters") val chapters: Int? = null,
    @SerialName("volumes") val volumes: Int? = null,
    @SerialName("text_html") val textHtml: String? = null,
    @SerialName("rewatches") val rewatches: Int? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
