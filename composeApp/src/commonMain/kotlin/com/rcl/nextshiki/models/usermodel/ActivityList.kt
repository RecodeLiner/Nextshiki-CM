package com.rcl.nextshiki.models.usermodel

import kotlinx.serialization.Serializable

@Serializable(with = ActivitySerializable::class)
data class ActivityList (
    val list : List<Activity>? = arrayListOf()
)