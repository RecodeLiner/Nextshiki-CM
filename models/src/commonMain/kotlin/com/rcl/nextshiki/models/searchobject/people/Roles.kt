package com.rcl.nextshiki.models.searchobject.people

import com.rcl.nextshiki.models.history.HistoryModelEntity
import com.rcl.nextshiki.models.searchobject.PeopleItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Roles(
    @SerialName("characters") val characters: List<PeopleItem> = emptyList(),
    @SerialName("animes") val animes: List<HistoryModelEntity> = emptyList()
)
