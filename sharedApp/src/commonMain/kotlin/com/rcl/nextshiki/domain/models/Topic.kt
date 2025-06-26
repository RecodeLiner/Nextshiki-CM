package com.rcl.nextshiki.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Topic(
    val id: Int?,
    val title: String?,
    val body: String?,
    val htmlBody: String?,
    val htmlFooter: String?,
    val createdAt: Instant?,
    val commentsCount: Int?,
    val forum: Forum?,
    val user: User?,
    val type: String?,
    val linkedContent: LinkedContent?,
    val viewed: Boolean?,
    val lastCommentViewed: Instant?,
    val event: String?,
    val episode: Int?
)