package com.rcl.nextshiki.base.search.mainsearchscreen

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel

interface IMainSearch {
    val typeList: List<SearchType>
    val currentPage: Value<Int>
    val possibleToAdd: Value<Boolean>
    val currentType: Value<SearchType>
    val searchedList: MutableList<SearchCardModel>
    val genresList: MutableList<GenreWithState>
    val text: Value<String>
    fun onTextChanged(value: String)
    fun updateType(type: SearchType)
    fun clearList()
    fun searchObject(text: String)
    fun updatePageList()
    fun setImpossibleToAdd()
    fun resetImpossibleToAdd()
    fun incPage()
    fun clearPage()
    fun navigateToSearchedObject(id: Int, type: SearchType)
}