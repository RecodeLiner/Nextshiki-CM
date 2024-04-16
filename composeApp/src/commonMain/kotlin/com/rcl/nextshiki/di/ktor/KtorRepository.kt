package com.rcl.nextshiki.di.ktor

import Nextshiki.composeApp.BuildConfig
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.intl.Locale
import com.rcl.nextshiki.models.calendar.CalendarModel
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.rcl.nextshiki.models.franchise.FranchiseModel
import com.rcl.nextshiki.models.friends.FriendModel
import com.rcl.nextshiki.models.genres.ListGenresItem
import com.rcl.nextshiki.models.history.HistoryModel
import com.rcl.nextshiki.models.moe.VideoLinkModel
import com.rcl.nextshiki.models.searchobject.PeopleKind
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.searchobject.anime.*
import com.rcl.nextshiki.models.searchobject.characters.CharacterModel
import com.rcl.nextshiki.models.searchobject.manga.MangaKind
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import com.rcl.nextshiki.models.searchobject.manga.MangaOrder
import com.rcl.nextshiki.models.searchobject.manga.MangaStatus
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.ranobe.RanobeObject
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.rcl.nextshiki.models.topics.ForumType
import com.rcl.nextshiki.models.topics.HotTopics
import com.rcl.nextshiki.models.topics.LinkedTypes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class KtorRepository(private val httpClient: HttpClient) {
    private val baseUrl = BuildConfig.DOMAIN
    private val moe = "https://anime.bytie.moe"
    suspend fun getCurrentUser(): CurrUserModel? {
        val url = "$baseUrl/api/users/whoami"
        return httpClient.get(url).body()
        //null if token is not setup
    }

    suspend fun signOut() {
        val url = "$baseUrl/api/users/sign_out"
        return httpClient.get(url).body()
    }

    suspend fun getGenres(): List<ListGenresItem> {
        val url = "$baseUrl/api/genres"
        return httpClient.get(url).body()
    }

    suspend fun getToken(
        isFirst: Boolean = false,
        code: String,
        clientID: String,
        clientSecret: String,
        redirectUri: String
    ): TokenModel {
        var url = "$baseUrl/oauth/token?"
        url += "grant_type=${if (isFirst) "authorization_code" else "refresh_token"}"
        url += "&client_id=${clientID}"
        url += "&client_secret=${clientSecret}"
        if (isFirst) {
            url += "&code=${code}"
            url += "&redirect_uri=${redirectUri}"
        } else {
            url += "&refresh_token=${code}"
        }
        return httpClient.post(url).body()
    }

    suspend fun searchPeople(search: String = "", peopleKind: PeopleKind? = null): List<SearchListItem> {
        var url = "$baseUrl/api/people/search"
        url += "?search=${search}"
        if (peopleKind != null) {
            url += "&peopleKind=${peopleKind.name.decapitalize(Locale("en"))}"
        }
        return httpClient.get(url).body()
    }

    suspend fun searchRanobe(
        search: String = "",
        page: Int = 1,
        limit: Int = 50,
        order: MangaOrder = MangaOrder.Ranked,
        kind: MangaKind? = null,
        status: MangaStatus? = null,
        season: String? = null,
        score: Int? = null,
        genre: String? = null,
        publisher: String? = null,
        franchise: String? = null,
        censored: Boolean = true,
        myList: AnimeMyListState? = null,
        ids: String? = null,
        excludeIds: String? = null,
    ): List<SearchListItem> {
        var url = "$baseUrl/api/ranobe"
        url += "?page=${page}"
        url += "&limit=${limit}"
        url += "&order=${order.name.decapitalize(Locale("en"))}"
        url += "&search=${search}"
        if (kind != null) {
            url += "&kind=${kind.name.decapitalize(Locale("en"))}"
        }
        if (status != null) {
            url += "&status=${status.name.decapitalize(Locale("en"))}"
        }
        if (season != null) {
            url += "&season=$season"
        }
        if (score != null) {
            url += "&score=$score"
        }
        if (genre != null) {
            url += "&genre=$genre"
        }
        if (publisher != null) {
            url += "&publisher=$publisher"
        }
        if (franchise != null) {
            url += "&franchise=$franchise"
        }
        url += "&censored=${censored.toString().decapitalize(Locale("en"))}"
        if (myList != null) {
            url += "&mylist=${myList.name.decapitalize(Locale("en"))}"
        }
        if (ids != null) {
            url += "&ids=$ids"
        }
        if (excludeIds != null) {
            url += "&excludeIds=$excludeIds"
        }
        return httpClient.get(url).body()
    }

    suspend fun searchUser(search: String = "", limit: Int = 100, page: Int = 1): List<SearchListItem> {
        var url = "$baseUrl/api/users"
        url += "?search=${search}"
        url += "&limit=$limit"
        url += "&page=$page"
        return httpClient.get(url).body()
    }

    suspend fun getHotTopics(limit: Int = 10) : List<HotTopics> {
        var url = "$baseUrl/api/topics/hot"
        url += "?limit=${limit}"
        return httpClient.get(url).body()
    }

    suspend fun getTopics(limit: Int = 30, page: Int = 1, linkedId: Int? = null, forum: ForumType? = null, linkedTypes: LinkedTypes? = null) : List<HotTopics> {
        var url = "$baseUrl/api/topics"
        url += "?page=$page"
        url += "&limit=$limit"
        if (forum != null){
            url += "&forum=${forum.name}"
        }
        if (linkedTypes != null && linkedId != null) {
            url += "&linked_type=${linkedTypes.name}"
            url += "&linked_id=$linkedId"
        }
        return httpClient.get(url).body()
    }

    suspend fun searchManga(
        search: String = "",
        page: Int = 1,
        limit: Int = 50,
        order: MangaOrder = MangaOrder.Ranked,
        kind: MangaKind? = null,
        status: MangaStatus? = null,
        season: String? = null,
        score: Int? = null,
        genre: String? = null,
        publisher: String? = null,
        franchise: String? = null,
        censored: Boolean = true,
        myList: AnimeMyListState? = null,
        ids: String? = null,
        excludeIds: String? = null,
    ): List<SearchListItem> {
        var url = "$baseUrl/api/mangas"
        url += "?page=${page}"
        url += "&limit=${limit}"
        url += "&order=${order.name.decapitalize(Locale("en"))}"
        url += "&search=${search}"
        if (kind != null) {
            url += "&kind=${kind.name.decapitalize(Locale("en"))}"
        }
        if (status != null) {
            url += "&status=${status.name.decapitalize(Locale("en"))}"
        }
        if (season != null) {
            url += "&season=$season"
        }
        if (score != null) {
            url += "&score=$score"
        }
        if (genre != null) {
            url += "&genre=$genre"
        }
        if (publisher != null) {
            url += "&publisher=$publisher"
        }
        if (franchise != null) {
            url += "&franchise=$franchise"
        }
        url += "&censored=${censored.toString().decapitalize(Locale("en"))}"
        if (myList != null) {
            url += "&mylist=${myList.name.decapitalize(Locale("en"))}"
        }
        if (ids != null) {
            url += "&ids=$ids"
        }
        if (excludeIds != null) {
            url += "&excludeIds=$excludeIds"
        }
        return httpClient.get(url).body()
    }

    suspend fun searchAnime(
        search: String = "",
        page: Int = 1,
        limit: Int = 50,
        order: AnimeOrder = AnimeOrder.Ranked,
        kind: AnimeKind? = null,
        status: AnimeStatus? = null,
        season: String? = null,
        score: Int? = null,
        duration: AnimeDuration? = null,
        rating: AnimeRating? = null,
        genre: String? = null,
        studio: String? = null,
        franchise: String? = null,
        censored: Boolean = true,
        myList: AnimeMyListState? = null,
        ids: String? = null,
        excludeIds: String? = null,
    ): List<SearchListItem> {
        var url = "$baseUrl/api/animes"
        url += "?page=${page}"
        url += "&limit=${limit}"
        url += "&order=${order.name.decapitalize(Locale("en"))}"
        url += "&search=${search}"
        if (kind != null) {
            url += "&kind=${kind.name.decapitalize(Locale("en"))}"
        }
        if (status != null) {
            url += "&status=${status.name.decapitalize(Locale("en"))}"
        }
        if (season != null) {
            url += "&season=$season"
        }
        if (score != null) {
            url += "&score=$score"
        }
        if (duration != null) {
            url += "&duration=${duration.name.decapitalize(Locale("en"))}"
        }
        if (rating != null) {
            url += "&rating=${rating.name.decapitalize(Locale("en"))}"
        }
        if (genre != null) {
            url += "&genre=$genre"
        }
        if (studio != null) {
            url += "&studio=$studio"
        }
        if (franchise != null) {
            url += "&franchise=$franchise"
        }
        url += "&censored=${censored.toString().decapitalize(Locale("en"))}"
        if (myList != null) {
            url += "&mylist=${myList.name.decapitalize(Locale("en"))}"
        }
        if (ids != null) {
            url += "&ids=$ids"
        }
        if (excludeIds != null) {
            url += "&excludeIds=$excludeIds"
        }

        return httpClient.get(url).body()
    }

    suspend fun searchCharacters(search: String): List<SearchListItem> {
        val url = "${baseUrl}/api/characters/search?search=$search"
        return httpClient.get(url).body()
    }

    suspend fun getAnimeById(id: String): AnimeObject {
        val url = "$baseUrl/api/animes/$id"
        return httpClient.get(url).body()
    }

    suspend fun getMangaById(id: String): MangaObject {
        val url = "$baseUrl/api/mangas/$id"
        return httpClient.get(url).body()
    }

    suspend fun getRanobeById(id: String): RanobeObject {
        val url = "$baseUrl/api/ranobe/$id"
        return httpClient.get(url).body()
    }

    suspend fun getPeopleById(id: String): PeopleObject {
        val url = "$baseUrl/api/people/$id"
        return httpClient.get(url).body()
    }

    suspend fun getUserById(id: String, isNickname: Boolean = true): UserObject {
        var url = "$baseUrl/api/users/$id"
        if (isNickname) {
            url += "?is_nickname=1"
        }
        return httpClient.get(url).body()
    }

    suspend fun getCalendar(): List<CalendarModel> {
        val url = "$baseUrl/api/calendar"
        return httpClient.get(url).body()
    }

    suspend fun getHistory(
        id: String,
        page: Int = 1,
        limit: Int = 100,
        targetId: String = "",
        targetType: String = ""
    ): List<HistoryModel> {
        var url = "$baseUrl/api/users/${id}/history"
        url += "?page=${page}"
        url += "&limit=${limit}"
        if (targetId != "") {
            url += "&target_id=${targetId}"
        }
        if (targetType != "") {
            url += "&target_type=${targetType}"
        }
        return httpClient.get(url).body()
    }

    suspend fun getCharacter(id: String): CharacterModel {
        val url = "$baseUrl/api/characters/${id}"
        return httpClient.get(url).body()
    }

    suspend fun friends(isAdd: Boolean, id: Int): FriendModel {
        val url = "$baseUrl/api/friends/${id}"
        return if (isAdd) {
            httpClient.post(url).body()
        } else {
            httpClient.delete(url).body()
        }
    }

    suspend fun ignore(isIgnore: Boolean, id: Int): FriendModel {
        val url = "$baseUrl/api/v2/users/${id}/ignore"
        return if (isIgnore) {
            httpClient.post(url).body()
        } else {
            httpClient.delete(url).body()
        }
    }

    suspend fun getVideoLinks(id: String): VideoLinkModel {
        val url = "${moe}/ext/search_by_id?shikimori_id=${id}"
        return httpClient.get(url) { headers {}.remove("Authorization") }.body()
    }

    suspend fun getAnimeFranchise(id: String): FranchiseModel {
        val url = "$baseUrl/api/animes/${id}/franchise"
        return httpClient.get(url).body()
    }
}