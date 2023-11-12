package com.rcl.nextshiki.di.ktor

import Nextshiki.composeApp.BuildConfig
import com.rcl.nextshiki.models.calendar.CalendarModel
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.rcl.nextshiki.models.franchise.FranchiseModel
import com.rcl.nextshiki.models.friends.FriendModel
import com.rcl.nextshiki.models.getLists.ListGenresItem
import com.rcl.nextshiki.models.history.HistoryModel
import com.rcl.nextshiki.models.moe.VideoLinkModel
import com.rcl.nextshiki.models.searchobject.CharacterModel
import com.rcl.nextshiki.models.searchobject.ObjById
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.usermodel.Userdata
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class KtorRepository(private val httpClient: HttpClient) {
    private val baseUrl = BuildConfig.DOMAIN
    private val moe = "https://anime.bytie.moe"
    suspend fun getCurrentUser(): CurrUserModel? {
        val url = "${baseUrl}/api/users/whoami"
        return httpClient.get(url).body()
    }

    suspend fun signOut() {
        val url = "${baseUrl}/api/users/sign_out"
        return httpClient.get(url).body()
    }

    suspend fun getGenres(): List<ListGenresItem> {
        val url = "${baseUrl}/api/genres"
        return httpClient.get(url).body()
    }

    suspend fun getUser(id: String, isNickname: Boolean): Userdata? {
        var url = "${baseUrl}/api/users/$id"
        if (isNickname) {
            url += "?is_nickname=1"
        }
        return httpClient.get(url).body()
    }

    suspend fun getObjectById(type: String, id: String): ObjById {
        val url = "${baseUrl}/api/${type}/${id}"
        return httpClient.get(url).body()
    }

    suspend fun getToken(
        isFirst: Boolean = false,
        code: String,
        clientID: String,
        clientSecret: String,
        redirectUri: String
    ): TokenModel {
        var url = "${baseUrl}/oauth/token?"
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

    suspend fun getSearchList(
        type: String, page: Int = 1, limit: Int = 50, order: String = "ranked", kind: String = "",
        status: String = "", duration: String = "", rating: String = "", season: String = "",
        score: String = "", genre: String = "",
        publisher: String = "", franchise: String = "", censored: Boolean = true,
        mylist: String = "", ids: String = "", excludeIds: String = "",
        search: String = ""
    ): List<SearchListItem> {
        var url = "${baseUrl}/api/${type}"
        url += "?page=${page}"
        url += "&limit=${limit}"
        url += "&order=${order}"
        if (kind != "") {
            url += "&kind=${kind}"
        }
        if (status != "") {
            url += "&status=${status}"
        }
        if (duration != "") {
            url += "&duration=${duration}"
        }
        if (rating != "") {
            url += "&rating=${rating}"
        }
        if (season != "") {
            url += "&season=${season}"
        }
        if (score != "") {
            url += "&score=${score}"
        }
        if (genre != "") {
            url += "&genre=${genre}"
        }
        if (publisher != "") {
            url += "&publisher=${publisher}"
        }
        if (franchise != "") {
            url += "&franchise=${franchise}"
        }
        if (!censored) {
            url += "&censored=${censored.toString().lowercase()}"
        }
        if (mylist != "") {
            url += "&mylist=${mylist}"
        }
        if (ids != "") {
            url += "&ids=${ids}"
        }
        if (excludeIds != "") {
            url += "&exclude_ids=${excludeIds}"
        }
        if (search != "") {
            url += "&search=${search}"
        }
        return httpClient.get(url).body()
    }

    suspend fun getCalendar(): List<CalendarModel> {
        val url = "${baseUrl}/api/calendar"
        return httpClient.get(url).body()
    }

    suspend fun getHistory(
        id: String,
        page: Int = 1,
        limit: Int = 100,
        target_id: String = "",
        target_type: String = ""
    ): List<HistoryModel> {
        var url = "${baseUrl}/api/users/${id}/history"
        url += "?page=${page}"
        url += "&limit=${limit}"
        if (target_id != "") {
            url += "&target_id=${target_id}"
        }
        if (target_type != "") {
            url += "&target_type=${target_type}"
        }
        return httpClient.get(url).body()
    }

    suspend fun getCharacter(id: String): CharacterModel {
        val url = "${baseUrl}/api/characters/${id}"
        return httpClient.get(url).body()
    }

    suspend fun friends(isAdd: Boolean, id: Int): FriendModel {
        val url = "${baseUrl}/api/friends/${id}"
        return if (isAdd) {
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
        val url = "${baseUrl}/api/animes/${id}/franchise"
        return httpClient.get(url).body()
    }
}