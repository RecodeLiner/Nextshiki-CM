package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterModel (
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("url") val url: String? = null,
    @SerialName("altname") val altname: String? = null,
    @SerialName("japanese") val japanese: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("description_html") val descriptionHtml: String? = null,
    @SerialName("description_source") val descriptionSource: String? = null,
    @SerialName("favoured") val favoured: Boolean? = null,
    @SerialName("thread_id") val threadId: Int? = null,
    @SerialName("topic_id") val topicId: Int? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("seyu") val seyu: ArrayList<Seyu> = arrayListOf(),
    @SerialName("animes") val animes: ArrayList<CharAnimes> = arrayListOf(),
    @SerialName("mangas") val mangas: ArrayList<CharMangas> = arrayListOf()
)