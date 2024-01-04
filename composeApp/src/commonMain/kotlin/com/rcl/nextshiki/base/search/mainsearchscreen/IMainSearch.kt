package com.rcl.nextshiki.base.search.mainsearchscreen

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.models.getLists.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel

interface IMainSearch {
    val typeList: List<SearchType>
    val currentType: Value<SearchType>
    val searchedList: MutableList<SearchCardModel>
    val genresList: MutableList<GenreWithState>
    var text: Value<String>
    fun onTextChanged(value: String)
    fun updateType(type: SearchType)
    fun clearList()
    fun searchObject(text: String)
}