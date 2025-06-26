package com.rcl.nextshiki.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class User(
    val id: Int?,
    val nickname: String?,
    val avatarUrl: String?,
    val imageUrls: UserImageUrls?,
    val lastOnlineAt: Instant?,
    val url: String?
)