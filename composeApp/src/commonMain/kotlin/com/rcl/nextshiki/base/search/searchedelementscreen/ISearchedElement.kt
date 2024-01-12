package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.ranobe.RanobeObject
import com.rcl.nextshiki.models.searchobject.users.UserObject

interface ISearchedElement {
    val searchedAnimeElement: Value<AnimeObject>
    val searchedMangaElement: Value<MangaObject>
    val searchedRanobeElement: Value<RanobeObject>
    val searchedPeopleElement: Value<PeopleObject>
    val searchedUserElement: Value<UserObject>
    fun popBack()
}