package com.rcl.nextshiki.base.search.mainsearchscreen

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.models.searchobject.SearchListItem

interface IMainSearch {
    val currentType: Value<SearchType>
    val list: MutableList<SearchListItem>
    var text: Value<String>
    fun onTextChanged(value: String)
    fun updateType(type: SearchType)
    fun clearList()
    fun searchObject(text: String)
}