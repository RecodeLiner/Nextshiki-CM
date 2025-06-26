package com.rcl.nextshiki.data.repository

import com.rcl.nextshiki.domain.models.CalendarEntry
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun getCalendarEntries(forceRefresh: Boolean): Flow<Result<List<CalendarEntry>>>
    suspend fun refreshCalendarEntries(): Result<Unit>
}