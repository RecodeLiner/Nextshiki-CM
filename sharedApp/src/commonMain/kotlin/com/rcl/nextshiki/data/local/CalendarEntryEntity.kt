package com.rcl.nextshiki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_entries")
data class CalendarEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nextEpisode: Int?,
    val nextEpisodeAt: String?,
    val duration: Int?,
    val animeJson: String?
)