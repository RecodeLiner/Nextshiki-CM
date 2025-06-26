package com.rcl.nextshiki.data.api

import com.rcl.nextshiki.data.dto.CalendarResponse
import io.ktor.client.call.body

class CalendarRemoteDataSourceImpl (
    private val httpClient: AuthorizedHttpClient
) : CalendarRemoteDataSource {
    override suspend fun getCalendar(): Result<List<CalendarResponse>> {
        return try {
            httpClient.get("/api/calendar").body()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}