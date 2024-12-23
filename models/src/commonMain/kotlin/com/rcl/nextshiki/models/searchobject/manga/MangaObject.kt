package com.rcl.nextshiki.models.searchobject.manga

import com.rcl.nextshiki.models.franchise.FranchiseModel
import com.rcl.nextshiki.models.genres.ListGenresItem
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.ISearchDescriptions
import com.rcl.nextshiki.models.searchobject.ISearchFranchise
import com.rcl.nextshiki.models.searchobject.ISearchRoles
import com.rcl.nextshiki.models.searchobject.ISearchScore
import com.rcl.nextshiki.models.searchobject.ISearchStatus
import com.rcl.nextshiki.models.searchobject.Publishers
import com.rcl.nextshiki.models.searchobject.RatesScoresStats
import com.rcl.nextshiki.models.searchobject.RolesClass
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.UserRate
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaObject(
    @SerialName("id") override val id: Int? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("russian") override val russian: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("url") override val url: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("score") override val score: String? = null,
    @SerialName("status") override val status: String? = null,
    @SerialName("volumes") val volumes: Int? = null,
    @SerialName("chapters") val chapters: Int? = null,
    @SerialName("aired_on") val airedOn: String? = null,
    @SerialName("released_on") val releasedOn: String? = null,
    @SerialName("english") val english: List<String?> = emptyList(),
    @SerialName("japanese") val japanese: List<String?>? = emptyList(),
    @SerialName("synonyms") val synonyms: List<String?>? = emptyList(),
    @SerialName("license_name_ru") val licenseNameRu: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("description_html") override val descriptionHtml: String? = null,
    @SerialName("description_source") override val descriptionSource: String? = null,
    @SerialName("franchise") val franchise: String? = null,
    @SerialName("favoured") val favoured: Boolean? = null,
    @SerialName("anons") val anons: Boolean? = null,
    @SerialName("ongoing") val ongoing: Boolean? = null,
    @SerialName("thread_id") val threadId: Int? = null,
    @SerialName("topic_id") val topicId: Int? = null,
    @SerialName("myanimelist_id") val myanimelistId: Int? = null,
    @SerialName("rates_scores_stats") val ratesScoresStats: List<RatesScoresStats> = emptyList(),
    @SerialName("rates_statuses_stats") val ratesStatusesStats: List<RatesScoresStats> = emptyList(),
    @SerialName("licensors") val licensors: List<String> = emptyList(),
    @SerialName("genres") val genres: List<ListGenresItem> = emptyList(),
    @SerialName("publishers") val publishers: List<Publishers> = emptyList(),
    @SerialName("user_rate") val userRate: UserRate? = UserRate(),

    //additional infos
    @SerialName("roles_list") override val rolesList: List<RolesClass> = emptyList(),
    @SerialName("franchise_model") override val franchiseModel: FranchiseModel? = null,
    override val searchType: SearchType = SearchType.Manga
) : CommonSearchInterface, ISearchStatus, ISearchScore, ISearchDescriptions, ISearchRoles,
    ISearchFranchise
