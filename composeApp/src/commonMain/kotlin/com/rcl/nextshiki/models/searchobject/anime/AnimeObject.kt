package com.rcl.nextshiki.models.searchobject.anime

import com.rcl.nextshiki.models.genres.ListGenresItem
import com.rcl.nextshiki.models.searchobject.RatesScoresStats
import com.rcl.nextshiki.models.searchobject.UserRate
import com.rcl.nextshiki.models.universal.Image
import com.rcl.nextshiki.models.usermodel.ContentScore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeObject (
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("russian") val russian: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("url") val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") val score: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("episodes_aired") val episodesAired: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("english") val english: List<String?> = arrayListOf(),
    @SerialName("japanese") val japanese: List<String?> = arrayListOf(),
    @SerialName("synonyms") val synonyms: List<String?> = arrayListOf(),
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
    @SerialName("rates_scores_stats") val ratesScoresStats: List<RatesScoresStats> = arrayListOf(),
    @SerialName("rates_statuses_stats") val ratesStatusesStats: List<ContentScore> = arrayListOf(),
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("next_episode_at") val nextEpisodeAt: String? = null,
    @SerialName("fansubbers") val fansubbers: List<String> = arrayListOf(),
    @SerialName("fandubbers") val fandubbers: List<String> = arrayListOf(),
    @SerialName("licensors") val licensors: List<String> = arrayListOf(),
    @SerialName("genres") val genres: List<ListGenresItem?> = arrayListOf(),
    @SerialName("studios") val studios: List<Studios> = arrayListOf(),
    @SerialName("videos") val videos: List<Videos> = arrayListOf(),
    @SerialName("screenshots") val screenshots: List<Screenshots> = arrayListOf(),
    @SerialName("user_rate") val userRate: UserRate? = UserRate()
)