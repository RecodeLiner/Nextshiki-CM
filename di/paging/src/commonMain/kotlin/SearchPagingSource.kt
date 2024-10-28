package com.rcl.nextshiki.di.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.models.searchobject.PeopleKind
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.anime.AnimeDuration
import com.rcl.nextshiki.models.searchobject.anime.AnimeKind
import com.rcl.nextshiki.models.searchobject.anime.AnimeMyListState
import com.rcl.nextshiki.models.searchobject.anime.AnimeOrder
import com.rcl.nextshiki.models.searchobject.anime.AnimeRating
import com.rcl.nextshiki.models.searchobject.anime.AnimeStatus
import com.rcl.nextshiki.models.searchobject.manga.MangaKind
import com.rcl.nextshiki.models.searchobject.manga.MangaOrder
import com.rcl.nextshiki.models.searchobject.manga.MangaStatus

class SearchPagingSource(
    private val repository: KtorRepository,
    private val searchType: SearchType,
    private val search: String = "",
    private val page: Int = 1,
    private val limit: Int = 50,
    private val order: Any? = null,
    private val additionalParams: Map<String, Any?> = emptyMap(),
    private val locale: String = "en"
) : PagingSource<Int, SearchListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchListItem> {
        val currentPage = params.key ?: page
        return try {
            val items = when (searchType) {
                SearchType.Anime -> repository.searchAnime(
                    search = search,
                    page = currentPage,
                    limit = limit,
                    order = order as? AnimeOrder ?: AnimeOrder.Ranked,
                    kind = additionalParams["kind"] as? AnimeKind,
                    status = additionalParams["status"] as? AnimeStatus,
                    season = additionalParams["season"] as? String,
                    score = additionalParams["score"] as? Int,
                    duration = additionalParams["duration"] as? AnimeDuration,
                    rating = additionalParams["rating"] as? AnimeRating,
                    genre = additionalParams["genre"] as? String,
                    studio = additionalParams["studio"] as? String,
                    franchise = additionalParams["franchise"] as? String,
                    censored = additionalParams["censored"] as? Boolean != false,
                    myList = additionalParams["myList"] as? AnimeMyListState,
                    ids = additionalParams["ids"] as? String,
                    excludeIds = additionalParams["excludeIds"] as? String,
                    locale = locale
                )
                SearchType.Manga -> repository.searchManga(
                    search = search,
                    page = currentPage,
                    limit = limit,
                    order = order as? MangaOrder ?: MangaOrder.Ranked,
                    kind = additionalParams["kind"] as? MangaKind,
                    status = additionalParams["status"] as? MangaStatus,
                    season = additionalParams["season"] as? String,
                    score = additionalParams["score"] as? Int,
                    genre = additionalParams["genre"] as? String,
                    publisher = additionalParams["publisher"] as? String,
                    franchise = additionalParams["franchise"] as? String,
                    censored = additionalParams["censored"] as? Boolean != false,
                    myList = additionalParams["myList"] as? AnimeMyListState,
                    ids = additionalParams["ids"] as? String,
                    excludeIds = additionalParams["excludeIds"] as? String,
                    locale = locale
                )
                SearchType.Ranobe -> repository.searchRanobe(
                    search = search,
                    page = currentPage,
                    limit = limit,
                    order = order as? MangaOrder ?: MangaOrder.Ranked,
                    kind = additionalParams["kind"] as? MangaKind,
                    status = additionalParams["status"] as? MangaStatus,
                    season = additionalParams["season"] as? String,
                    score = additionalParams["score"] as? Int,
                    genre = additionalParams["genre"] as? String,
                    publisher = additionalParams["publisher"] as? String,
                    franchise = additionalParams["franchise"] as? String,
                    censored = additionalParams["censored"] as? Boolean != false,
                    myList = additionalParams["myList"] as? AnimeMyListState,
                    ids = additionalParams["ids"] as? String,
                    excludeIds = additionalParams["excludeIds"] as? String,
                    locale = locale
                )
                SearchType.Users -> repository.searchUser(
                    search = search,
                    limit = limit,
                    page = currentPage,
                    locale = locale
                )
                SearchType.People -> repository.searchPeople(
                    search = search,
                    peopleKind = additionalParams["peopleKind"] as? PeopleKind,
                    locale = locale
                )
                SearchType.Characters -> repository.searchCharacters(
                    search = search,
                    locale = locale
                )
            }

            val nextKey = if (items.isEmpty()) null else currentPage + 1

            LoadResult.Page(
                data = items,
                prevKey = if (currentPage == page) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
