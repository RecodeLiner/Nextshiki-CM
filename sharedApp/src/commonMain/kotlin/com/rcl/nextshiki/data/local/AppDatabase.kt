package com.rcl.nextshiki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rcl.nextshiki.data.mappers.RoomConverter

@Database(entities = [CalendarEntryEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calendarEntryDao(): CalendarEntryDao
}