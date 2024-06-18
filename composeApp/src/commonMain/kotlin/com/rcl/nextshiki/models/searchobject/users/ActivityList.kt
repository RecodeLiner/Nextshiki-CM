package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.Serializable

@Serializable(with = ActivitySerializable::class)
data class ActivityList(
    val list: List<Activity>? = listOf()
)