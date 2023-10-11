package com.rcl.nextshiki.screens.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.getLists.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchListItem
import kotlinx.coroutines.launch

class SearchViewModel : ScreenModel {
    val prepText = mutableStateOf("")
    val page = mutableStateOf(0)
    val hasNext = mutableStateOf(false)
    val listWithState = mutableStateListOf<GenreWithState>()
    val listContent = mutableStateListOf<SearchListItem>()

    init {
        coroutineScope.launch {
            //get a Genre list
            if (listWithState.isEmpty()) {
                val list = koin.get<KtorRepository>().getGenres()
                list.forEach {
                    listWithState.add(GenreWithState(it))
                }
            }
            //get anime samples
            getBasicList()
        }
    }

    suspend fun getBasicList(search: String = ""){
        listContent.addAll(koin.get<KtorRepository>().getSearchList(search = search, type = "animes"))
    }

    fun clearList() {
        listContent.clear()
        page.value = 0
    }

    fun getContent(name: String, type: String, page: Int) {
        if (hasNext.value) {
            coroutineScope.launch {
                val list = koin.get<KtorRepository>().getSearchList(type = type, search = name, page = page)
                if (list.isEmpty() || listContent.contains(list.last())) {
                    hasNext.value = false
                } else {
                    listContent.addAll(list)
                }
            }
        }
    }
}