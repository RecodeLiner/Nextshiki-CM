package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.searchobject.SearchListItem
import io.github.aakira.napier.Napier
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val SearchType.getTypeValue: String
    get() {
        return when (this) {
            SearchType.Anime -> "animes"
            SearchType.Manga -> "mangas"
            SearchType.Ranobe -> "ranobe"
            SearchType.People -> "people"
            SearchType.Users -> "users"
        }
    }

class MainSearchComponent(context: ComponentContext) : ComponentContext by context, IMainSearch {
    private val _text = MutableValue("")

    private val scope = coroutineScope(MainScope().coroutineContext + SupervisorJob())

    private val _currentType = MutableValue(SearchType.Anime)
    override val currentType: Value<SearchType> = _currentType
    override val list = mutableStateListOf<SearchListItem>()
    override var text: Value<String> = _text

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    searchObject(text = "")
                }
            }
        )
    }

    override fun onTextChanged(value: String) {
        clearList()
        _text.value = value
    }

    override fun updateType(type: SearchType) {
        _currentType.value = type
    }

    override fun clearList() {
        list.clear()
    }

    override fun searchObject(text: String) {
        clearList()
        scope.launch {
            list.addAll(
                koin.get<KtorRepository>()
                    .getSearchList(type = currentType.value.getTypeValue, search = text)
            )
            Napier.i(list.joinToString(". "))
        }
    }
}