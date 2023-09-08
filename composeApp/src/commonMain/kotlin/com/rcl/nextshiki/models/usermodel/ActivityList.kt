package com.rcl.nextshiki.models.usermodel

import kotlinx.serialization.Serializable

@Serializable(with = ActivitySerializable::class)
class ActivityList : ArrayList<Activity>()