package com.rcl.nextshiki.models.searchobject.people

import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeopleObject (
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null,
    @SerialName("japanese") val japanese: String? = null,
    @SerialName("job_title") val jobTitle: String? = null,
    @SerialName("birth_on") val birthOn: Birthday? = Birthday(),
    @SerialName("deceased_on") val deceasedOn: Birthday? = Birthday(),
    @SerialName("website") val website: String? = null,
    @SerialName("groupped_roles") val grouppedRoles: List<String> = listOf(),
    @SerialName("roles") val roles: List<Roles> = listOf(),
    @SerialName("works") val works: List<Works> = listOf(),
    @SerialName("topic_id") val topicId: Int? = null,
    @SerialName("person_favoured") val personFavoured: Boolean? = null,
    @SerialName("producer") val producer: Boolean? = null,
    @SerialName("producer_favoured") val producerFavoured: Boolean? = null,
    @SerialName("mangaka") val mangaka: Boolean? = null,
    @SerialName("mangaka_favoured") val mangakaFavoured: Boolean? = null,
    @SerialName("seyu") val seyu: Boolean? = null,
    @SerialName("seyu_favoured") val seyuFavoured: Boolean? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("thread_id") val threadId: Int? = null,
    @SerialName("birthday") val birthday: Birthday? = Birthday()
) : CommonSearchInterface