package com.rcl.nextshiki.models.searchobject.characters

import com.rcl.nextshiki.models.searchobject.CharAnimes
import com.rcl.nextshiki.models.searchobject.CharMangas
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.ISearchDescriptions
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.Seyu
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterModel(
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null,
    @SerialName("altname") val altname: String? = null,
    @SerialName("japanese") val japanese: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("description_html") override val descriptionHtml: String? = null,
    @SerialName("description_source") override val descriptionSource: String? = null,
    @SerialName("favoured") val favoured: Boolean? = null,
    @SerialName("thread_id") val threadId: Int? = null,
    @SerialName("topic_id") val topicId: Int? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("seyu") val seyu: List<Seyu> = emptyList(),
    @SerialName("animes") val animes: List<CharAnimes> = emptyList(),
    @SerialName("mangas") val mangas: List<CharMangas> = emptyList(),
) : CommonSearchInterface, ISearchDescriptions {
    override val searchType: SearchType
        get() = SearchType.Characters
}
