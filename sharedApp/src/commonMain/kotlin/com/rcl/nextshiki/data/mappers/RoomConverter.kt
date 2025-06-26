package com.rcl.nextshiki.data.mappers

import androidx.room.TypeConverter
import com.rcl.nextshiki.domain.models.Anime
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RoomConverter {
    @TypeConverter
    fun fromInstantToString(value: Instant?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun fromStringToInstant(value: String?): Instant? {
        return value?.toLong()?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromStringToLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromAnimeToString(anime: Anime?): String? {
        return Json.encodeToString(anime)
    }

    @TypeConverter
    fun fromStringToAnime(json: String?): Anime? {
        return json?.let { Json.decodeFromString(it) }
    }
}