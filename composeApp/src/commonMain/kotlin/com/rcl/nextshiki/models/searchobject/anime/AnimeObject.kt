package com.rcl.nextshiki.models.searchobject.anime

import com.rcl.nextshiki.models.genres.ListGenresItem
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.RatesScoresStats
import com.rcl.nextshiki.models.searchobject.RolesClass
import com.rcl.nextshiki.models.searchobject.UserRate
import com.rcl.nextshiki.models.searchobject.users.ContentScore
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeObject(
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") val score: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("episodes_aired") val episodesAired: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("english") val english: List<String?> = listOf(),
    @SerialName("japanese") val japanese: List<String?> = listOf(),
    @SerialName("synonyms") val synonyms: List<String?> = listOf(),
    @SerialName("license_name_ru") val licenseNameRu: String? = null,
    @SerialName("duration") val duration: Int? = null,
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
    @SerialName("rates_scores_stats") val ratesScoresStats: List<RatesScoresStats> = listOf(),
    @SerialName("rates_statuses_stats") val ratesStatusesStats: List<ContentScore> = listOf(),
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("next_episode_at") val nextEpisodeAt: String? = null,
    @SerialName("fansubbers") val fansubbers: List<String> = listOf(),
    @SerialName("fandubbers") val fandubbers: List<String> = listOf(),
    @SerialName("licensors") val licensors: List<String> = listOf(),
    @SerialName("genres") val genres: List<ListGenresItem> = listOf(),
    @SerialName("studios") val studios: List<Studios> = listOf(),
    @SerialName("videos") val videos: List<Videos> = listOf(),
    @SerialName("screenshots") val screenshots: List<Screenshots> = listOf(),
    @SerialName("user_rate") val userRate: UserRate? = UserRate(),

    //additional infos
    @SerialName("roles_list") val rolesList: List<RolesClass> = listOf(),
) : CommonSearchInterface