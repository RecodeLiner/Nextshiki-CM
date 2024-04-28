package com.rcl.nextshiki.di.ktor

import Nextshiki.composeApp.BuildConfig
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.intl.Locale
import com.rcl.nextshiki.models.calendar.CalendarModel
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.currentuser.TokenModel
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
import com.rcl.nextshiki.models.topics.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class KtorRepository(private val httpClient: HttpClient) {
    private val baseUrl = BuildConfig.DOMAIN
    private val moe = "https://anime.bytie.moe"
    suspend fun getCurrentUser() = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/users/whoami"
        httpClient.get(url).body<CurrUserModel?>()
        //null if token is not setup
    }

    suspend fun signOut() {
        val url = "$baseUrl/api/users/sign_out"
        return httpClient.get(url).body()
    }

    suspend fun getGenres(locale: String = "en") = withContext(Dispatchers.IO) {
        var url = "$baseUrl/api/genres"
        url += "?locale=${locale}"
        httpClient.get(url).body<List<ListGenresItem>>()
    }

    suspend fun getToken(
        isFirst: Boolean = false,
        code: String,
        clientID: String,
        clientSecret: String,
        redirectUri: String
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/oauth/token?")
            append("grant_type=${if (isFirst) "authorization_code" else "refresh_token"}")
            append("&client_id=${clientID}")
            append("&client_secret=${clientSecret}")
            if (isFirst) {
                append("&code=${code}")
                append("&redirect_uri=${redirectUri}")
            } else {
                append("&refresh_token=${code}")
            }
        }
        httpClient.post(url.toString()).body<TokenModel>()
    }

    suspend fun searchPeople(search: String = "", peopleKind: PeopleKind? = null, locale: String = "en") =
        withContext(Dispatchers.IO) {
            val url = StringBuilder()
            url.apply {
                append("$baseUrl/api/people/search")
                append("?search=${search}")
                if (peopleKind != null) {
                    append("&peopleKind=${peopleKind.name.decapitalize(Locale("en"))}")
                }
                append("&locale=${locale}")
            }
            httpClient.get(url.toString()).body<List<SearchListItem>>()
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
        locale: String = "en"
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/ranobe")
            append("?page=${page}")
            append("&limit=${limit}")
            append("&order=${order.name.decapitalize(Locale("en"))}")
            append("&search=${search}")
            if (kind != null) {
                append("&kind=${kind.name.decapitalize(Locale("en"))}")
            }
            if (status != null) {
                append("&status=${status.name.decapitalize(Locale("en"))}")
            }
            if (season != null) {
                append("&season=$season")
            }
            if (score != null) {
                append("&score=$score")
            }
            if (genre != null) {
                append("&genre=$genre")
            }
            if (publisher != null) {
                append("&publisher=$publisher")
            }
            if (franchise != null) {
                append("&franchise=$franchise")
            }
            append("&censored=${censored.toString().decapitalize(Locale("en"))}")
            if (myList != null) {
                append("&mylist=${myList.name.decapitalize(Locale("en"))}")
            }
            if (ids != null) {
                append("&ids=$ids")
            }
            if (excludeIds != null) {
                append("&excludeIds=$excludeIds")
            }
            append("&locale=${locale}")
        }
        httpClient.get(url.toString()).body<List<SearchListItem>>()
    }

    suspend fun searchUser(search: String = "", limit: Int = 100, page: Int = 1, locale: String = "en") =
        withContext(Dispatchers.IO) {
            val url = StringBuilder()
            url.apply {
                append("$baseUrl/api/users")
                append("?search=${search}")
                append("&limit=$limit")
                append("&page=$page")
                append("&locale=${locale}")
            }
            httpClient.get(url.toString()).body<List<SearchListItem>>()
        }

    suspend fun getHotTopics(limit: Int = 10, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/topics/hot")
            append("?limit=${limit}")
            append("&locale=${locale}")
        }
        httpClient.get(url.toString()).body<List<HotTopics>>()
    }

    suspend fun getTopics(
        limit: Int = 30,
        page: Int = 1,
        linkedId: Int? = null,
        forum: ForumType? = null,
        linkedTypes: LinkedTypes? = null,
        locale: String = "en"
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/topics")
            append("?page=$page")
            append("&limit=$limit")
            if (forum != null)
                append("&forum=${forum.name}")
            if (linkedTypes != null && linkedId != null) {
                append("&linked_type=${linkedTypes.name}")
                append("&linked_id=$linkedId")
            }
            append("&locale=${locale}")
        }
        httpClient.get(url.toString()).body<List<HotTopics>>()
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
        locale: String = "en"
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/mangas")
            append("?page=${page}")
            append("&limit=${limit}")
            append("&order=${order.name.decapitalize(Locale("en"))}")
            append("&search=${search}")
            if (kind != null) {
                append("&kind=${kind.name.decapitalize(Locale("en"))}")
            }
            if (status != null) {
                append("&status=${status.name.decapitalize(Locale("en"))}")
            }
            if (season != null) {
                append("&season=$season")
            }
            if (score != null) {
                append("&score=$score")
            }
            if (genre != null) {
                append("&genre=$genre")
            }
            if (publisher != null) {
                append("&publisher=${publisher}")
            }
            if (franchise != null) {
                append("&franchise=$franchise")
            }
            append("&censored=${censored.toString().decapitalize(Locale("en"))}")
            if (myList != null) {
                append("&mylist=${myList.name.decapitalize(Locale("en"))}")
            }
            if (ids != null) {
                append("&ids=$ids")
            }
            if (excludeIds != null) {
                append("&excludeIds=$excludeIds")
            }
            append("&locale=${locale}")
        }
        httpClient.get(url.toString()).body<List<SearchListItem>>()
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
        locale: String = "en"
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/animes")
            append("?page=${page}")
            append("&limit=${limit}")
            append("&order=${order.name.decapitalize(Locale("en"))}")
            append("&search=${search}")
            if (kind != null) {
                append("&kind=${kind.name.decapitalize(Locale("en"))}")
            }
            if (status != null) {
                append("&status=${status.name.decapitalize(Locale("en"))}")
            }
            if (season != null) {
                append("&season=$season")
            }
            if (score != null) {
                append("&score=$score")
            }
            if (genre != null) {
                append("&genre=$genre")
            }
            if (franchise != null) {
                append("&franchise=$franchise")
            }
            append("&censored=${censored.toString().decapitalize(Locale("en"))}")
            if (myList != null) {
                append("&mylist=${myList.name.decapitalize(Locale("en"))}")
            }
            if (duration != null) {
                append("&duration=${duration.name.decapitalize(Locale("en"))}")
            }
            if (rating != null) {
                append("&rating=${rating.name.decapitalize(Locale("en"))}")
            }
            if (studio != null) {
                append("&studio=$studio")
            }
            if (ids != null) {
                append("&ids=$ids")
            }
            if (excludeIds != null) {
                append("&excludeIds=$excludeIds")
            }
            append("&locale=${locale}")
        }

        httpClient.get(url.toString()).body<List<SearchListItem>>()
    }

    suspend fun searchCharacters(search: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "${baseUrl}/api/characters/search?search=$search&locale=$locale"
        httpClient.get(url).body<List<SearchListItem>>()
    }

    suspend fun getAnimeById(id: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/animes/$id?locale=$locale"
        httpClient.get(url).body<AnimeObject>()
    }

    suspend fun getMangaById(id: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/mangas/$id?locale=$locale"
        httpClient.get(url).body<MangaObject>()
    }

    suspend fun getRanobeById(id: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/ranobe/$id?locale=$locale"
        httpClient.get(url).body<RanobeObject>()
    }

    suspend fun getPeopleById(id: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/people/$id?locale=$locale"
        httpClient.get(url).body<PeopleObject>()
    }

    suspend fun getUserById(id: String, isNickname: Boolean = true, locale: String = "en") =
        withContext(Dispatchers.IO) {
            val url = StringBuilder()
            url.apply {
                append("$baseUrl/api/users/$id")
                append("?locale=$locale")
                if (isNickname) {
                    append("&is_nickname=1")
                }
            }
            httpClient.get(url.toString()).body<UserObject>()
        }

    suspend fun getCalendar(locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/calendar?locale=$locale"
        httpClient.get(url).body<List<CalendarModel>>()
    }

    suspend fun getHistory(
        id: String,
        page: Int = 1,
        limit: Int = 100,
        targetId: String = "",
        targetType: String = "",
        locale: String = "en"
    ) = withContext(Dispatchers.IO) {
        val url = StringBuilder()
        url.apply {
            append("$baseUrl/api/users/${id}/history")
            append("?page=${page}")
            append("&limit=${limit}")
            if (targetId.isNotEmpty()) {
                append("&target_id=${targetId}")
            }
            if (targetType.isNotEmpty()) {
                append("&target_type=${targetType}")
            }
            append("&locale=${locale}")
        }
        httpClient.get(url.toString()).body<List<HistoryModel>>()
    }

    suspend fun getCharacter(id: String, locale: String = "en") = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/characters/${id}?locale=$locale"
        httpClient.get(url).body<CharacterModel>()
    }

    suspend fun friends(isAdd: Boolean, id: Int, locale: String = "en") = withContext(Dispatchers.Main) {
        val url = "$baseUrl/api/friends/${id}?locale=$locale"
        if (isAdd) {
            httpClient.post(url).body<FriendModel>()
        } else {
            httpClient.delete(url).body<FriendModel>()
        }
    }

    suspend fun getFriendList(id: Int, locale: String = "en", limit: Int = 100, page: Int = 1) = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/users/${id}/friends?locale=$locale&limit=$limit&page=$page"
        httpClient.get(url).body<List<User>>()
    }

    suspend fun ignore(isIgnore: Boolean, id: Int, locale: String = "en") = withContext(Dispatchers.Main) {
        val url = "$baseUrl/api/v2/users/${id}/ignore?locale=$locale"
        if (isIgnore) {
            httpClient.post(url).body<FriendModel>()
        } else {
            httpClient.delete(url).body<FriendModel>()
        }
    }

    suspend fun getVideoLinks(id: String): VideoLinkModel {
        val url = "${moe}/ext/search_by_id?shikimori_id=${id}"
        return httpClient.get(url) { headers.clear() }.body()
    }
}