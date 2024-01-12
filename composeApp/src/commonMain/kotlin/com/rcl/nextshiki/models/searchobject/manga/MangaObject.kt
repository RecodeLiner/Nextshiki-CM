package com.rcl.nextshiki.models.searchobject.manga

import com.rcl.nextshiki.models.genres.ListGenresItem
import com.rcl.nextshiki.models.searchobject.Publishers
import com.rcl.nextshiki.models.searchobject.RatesScoresStats
import com.rcl.nextshiki.models.searchobject.UserRate
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaObject (
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("url") val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") val score: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("volumes") val volumes: Int? = null,
    @SerialName("chapters") val chapters: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("english") val english: ArrayList<String> = arrayListOf(),
    @SerialName("japanese") val japanese: ArrayList<String> = arrayListOf(),
    @SerialName("synonyms") val synonyms: ArrayList<String> = arrayListOf(),
    @SerialName("license_name_ru") val licenseNameRu: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("description_html") val descriptionHtml: String? = null,
    @SerialName("description_source") val descriptionSource: String? = null,
    @SerialName("franchise") val franchise: String? = null,
    @SerialName("favoured") val favoured: Boolean? = null,
    @SerialName("anons") val anons: Boolean? = null,
    @SerialName("ongoing") val ongoing: Boolean? = null,
    @SerialName("thread_id") val threadId: Int? = null,
    @SerialName("topic_id") val topicId: Int? = null,
    @SerialName("myanimelist_id") val myanimelistId: Int? = null,
    @SerialName("rates_scores_stats") val ratesScoresStats: ArrayList<RatesScoresStats> = arrayListOf(),
    @SerialName("rates_statuses_stats") val ratesStatusesStats: ArrayList<RatesScoresStats> = arrayListOf(),
    @SerialName("licensors") val licensors: ArrayList<String> = arrayListOf(),
    @SerialName("genres") val genres: ArrayList<ListGenresItem> = arrayListOf(),
    @SerialName("publishers") val publishers: ArrayList<Publishers> = arrayListOf(),
    @SerialName("user_rate") val userRate: UserRate? = UserRate()

)