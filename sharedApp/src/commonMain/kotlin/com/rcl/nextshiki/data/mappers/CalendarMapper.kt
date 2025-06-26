package com.rcl.nextshiki.data.mappers

import com.rcl.nextshiki.data.dto.AnimeDto
import com.rcl.nextshiki.data.dto.CalendarResponse
import com.rcl.nextshiki.data.dto.ImageDto
import com.rcl.nextshiki.data.local.CalendarEntryEntity
import com.rcl.nextshiki.domain.models.Anime
import com.rcl.nextshiki.domain.models.CalendarEntry
import com.rcl.nextshiki.domain.models.ImageUrls
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun CalendarResponse.toDomain(): CalendarEntry {
    return CalendarEntry(
        nextEpisode = this.nextEpisode,
        nextEpisodeAt = this.nextEpisodeAt?.let { parseInstant(it) },
        duration = this.duration,
        anime = this.anime?.toDomain()
    )
}

fun AnimeDto.toDomain(): Anime {
    return Anime(
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

fun ImageDto.toDomain(): ImageUrls {
    return ImageUrls(
        original = this.original,
        preview = this.preview,
        x96 = this.x96,
        x48 = this.x48
    )
}

@OptIn(ExperimentalTime::class)
fun parseInstant(dateString: String): Instant? {
    return try {
        Instant.parse(dateString)
    } catch (e: IllegalArgumentException) {
        // TODO: Log error
        null
    }
}

fun parseLocalDate(dateString: String): LocalDate? {
    return try {
        LocalDate.parse(dateString)
    } catch (e: IllegalArgumentException) {
        // TODO: Log error
        null
    }
}

@OptIn(ExperimentalTime::class)
fun CalendarEntryEntity.toDomain(): CalendarEntry {
    return CalendarEntry(
        nextEpisode = nextEpisode,
        nextEpisodeAt = nextEpisodeAt?.let { Instant.parse(it) },
        duration = duration,
        anime = animeJson?.let { Json.decodeFromString<Anime>(it) }
    )
}

@OptIn(ExperimentalTime::class)
fun CalendarEntry.toEntity(id: Long = 0): CalendarEntryEntity {
    return CalendarEntryEntity(
        id = id,
        nextEpisode = nextEpisode,
        nextEpisodeAt = nextEpisodeAt.toString(),
        duration = duration,
        animeJson = anime?.let { Json.encodeToString(it) }
    )
}