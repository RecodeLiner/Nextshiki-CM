package com.rcl.nextshiki.models.searchobject.people

import com.rcl.nextshiki.models.history.HistoryModelEntity
import com.rcl.nextshiki.models.searchobject.PeopleItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Roles(
    @SerialName("characters") val characters: ArrayList<PeopleItem> = arrayListOf(),
    @SerialName("animes") val animes: ArrayList<HistoryModelEntity> = arrayListOf()
)