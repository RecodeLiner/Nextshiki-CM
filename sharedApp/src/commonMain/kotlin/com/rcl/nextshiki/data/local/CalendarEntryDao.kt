package com.rcl.nextshiki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEntryDao {
    @Query("SELECT * FROM calendar_entries ORDER BY nextEpisodeAt ASC")
    fun getAllCalendarEntries(): Flow<List<CalendarEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CalendarEntryEntity>)

    @Update
    suspend fun update(entry: CalendarEntryEntity)

    @Query("DELETE FROM calendar_entries")
    suspend fun deleteAll()

    @Query("SELECT * FROM calendar_entries WHERE id = :id")
    suspend fun getCalendarEntryById(id: Long): CalendarEntryEntity?

    @Query("SELECT id FROM calendar_entries")
    suspend fun getAllCalendarEntryIds(): List<Long>

    @Query("DELETE FROM calendar_entries WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("SELECT * FROM calendar_entries WHERE nextEpisodeAt = :nextEpisodeAt AND animeJson LIKE :animeJsonPart")
    suspend fun getCalendarEntryByUniqueFields(nextEpisodeAt: String, animeJsonPart: String): CalendarEntryEntity?
}