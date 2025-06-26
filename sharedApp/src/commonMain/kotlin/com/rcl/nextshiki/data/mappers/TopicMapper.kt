package com.rcl.nextshiki.data.mappers

import TopicResponse
import com.rcl.nextshiki.data.dto.ForumDto
import com.rcl.nextshiki.data.dto.LinkedAnimeDto
import com.rcl.nextshiki.data.dto.UserDto
import com.rcl.nextshiki.data.dto.UserImageDto
import com.rcl.nextshiki.domain.models.Forum
import com.rcl.nextshiki.domain.models.LinkedContent
import com.rcl.nextshiki.domain.models.Topic
import com.rcl.nextshiki.domain.models.User
import com.rcl.nextshiki.domain.models.UserImageUrls
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun TopicResponse.toDomain(): Topic {
    return Topic(
        id = this.id,
        title = this.topicTitle,
        body = this.body,
        htmlBody = this.htmlBody,
        htmlFooter = this.htmlFooter,
        createdAt = this.createdAt?.let { parseInstant(it) },
        commentsCount = this.commentsCount,
        forum = this.forum?.toDomain(),
        user = this.user?.toDomain(),
        type = this.type,
        linkedContent = this.linked?.toDomain(),
        viewed = this.viewed,
        lastCommentViewed = this.lastCommentViewed?.let { parseInstant(it) },
        event = this.event,
        episode = this.episode
    )
}

fun ForumDto.toDomain(): Forum {
    return Forum(
        id = this.id,
        position = this.position,
        name = this.name,
        permalink = this.permalink,
        url = this.url
    )
}

@OptIn(ExperimentalTime::class)
fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        nickname = this.nickname,
        avatarUrl = this.avatar,
        imageUrls = this.image?.toDomain(),
        lastOnlineAt = this.lastOnlineAt?.let { parseInstant(it) },
        url = this.url
    )
}

fun UserImageDto.toDomain(): UserImageUrls {
    return UserImageUrls(
        x160 = this.x160,
        x148 = this.x148,
        x80 = this.x80,
        x64 = this.x64,
        x48 = this.x48,
        x32 = this.x32,
        x16 = this.x16
    )
}

fun LinkedAnimeDto.toDomain(): LinkedContent {
    return LinkedContent(
        id = this.id,
        name = this.name,
        russianName = this.russian,
        imageUrls = this.image?.toDomain(),
        url = this.url,
        kind = this.kind,
        score = this.score?.toFloatOrNull(),
        status = this.status,
        episodes = this.episodes,
        episodesAired = this.episodesAired,
        airedOn = this.airedOn?.let { parseLocalDate(it) },
        releasedOn = this.releasedOn?.let { parseLocalDate(it) }
    )
}
