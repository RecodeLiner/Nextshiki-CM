package com.rcl.nextshiki.domain.usecases

import com.rcl.nextshiki.data.repository.CalendarRepository
import com.rcl.nextshiki.domain.models.CalendarEntry
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetCalendarUseCase(
    private val repository: CalendarRepository
) {
    operator fun invoke(forceRefresh: Boolean): Flow<Result<List<CalendarEntry>>> = flow {
        val refreshJob = coroutineScope {
            launch {
                println("GetCalendarUseCase: Initiating refresh...")
                val refreshResult = repository.refreshCalendarEntries()
                if (refreshResult.isFailure) {
                    println("GetCalendarUseCase: Refresh failed: ${refreshResult.exceptionOrNull()?.message}")
                } else {
                    println("GetCalendarUseCase: Refresh completed successfully.")
                }
            }
        }

        repository.getCalendarEntries(forceRefresh)
            .collect { result ->
                emit(result)
            }

        refreshJob.join()
    }
}