package com.rcl.nextshiki.models.searchobject.people

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeopleObject (
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("url") val url: String? = null,
    @SerialName("japanese") val japanese: String? = null,
    @SerialName("job_title") val jobTitle: String? = null,
    @SerialName("birth_on") val birthOn: Birthday? = Birthday(),
    @SerialName("deceased_on") val deceasedOn: Birthday? = Birthday(),
    @SerialName("website") val website: String? = null,
    @SerialName("groupped_roles") val grouppedRoles: ArrayList<String> = arrayListOf(),
    @SerialName("roles") val roles: ArrayList<Roles> = arrayListOf(),
    @SerialName("works") val works: ArrayList<Works> = arrayListOf(),
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
)