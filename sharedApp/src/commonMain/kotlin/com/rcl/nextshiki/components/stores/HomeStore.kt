package com.rcl.nextshiki.components.stores

import com.arkivanov.mvikotlin.core.store.Store
import com.rcl.nextshiki.domain.models.CalendarEntry

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

    sealed interface Intent {
        data class LoadCalendar(val forceRefresh: Boolean) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val calendarEntries: List<CalendarEntry> = emptyList(),
        val error: String? = null
    )

    sealed interface Label {
    }
}