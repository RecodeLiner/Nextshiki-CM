package com.rcl.nextshiki.data.api

import com.rcl.nextshiki.data.dto.CalendarResponse

interface CalendarRemoteDataSource {
    suspend fun getCalendar(): Result<List<CalendarResponse>>
}