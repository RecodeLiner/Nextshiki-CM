package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.models.searchobject.ObjById

interface ISearchedElement {
    val searchedElement: Value<ObjById>
}