package com.rcl.nextshiki.components.factories

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rcl.nextshiki.components.factories.HomeStoreFactory.Msg.CalendarUpdated
import com.rcl.nextshiki.components.factories.HomeStoreFactory.Msg.Error
import com.rcl.nextshiki.components.factories.HomeStoreFactory.Msg.Loading
import com.rcl.nextshiki.components.stores.HomeStore
import com.rcl.nextshiki.domain.models.CalendarEntry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val homeDependencies: HomeDependencies
) {
    fun create(): HomeStore =
        object : HomeStore,
            Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
                name = "HomeStore",
                initialState = HomeStore.State(),
                bootstrapper = SimpleBootstrapper(Action.LoadCalendarOnInit),
                executorFactory = { ExecutorImpl(homeDependencies) },
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data object LoadCalendarOnInit : Action
        data class CalendarLoading(val isLoading: Boolean) : Action
        data class CalendarLoaded(val entries: List<CalendarEntry>) : Action
    }

    private sealed interface Msg {
        data class Loading(val isLoading: Boolean) : Msg
        data class CalendarUpdated(val entries: List<CalendarEntry>) : Msg
        data class Error(val error: String?) : Msg
    }

    private class ExecutorImpl(
        private val homeDependencies: HomeDependencies
    ) : CoroutineExecutor<HomeStore.Intent, Action, HomeStore.State, Msg, HomeStore.Label>() {

        override fun executeIntent(intent: HomeStore.Intent) {
            when (intent) {
                is HomeStore.Intent.LoadCalendar -> loadCalendar(intent.forceRefresh)
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadCalendarOnInit -> loadCalendar(false)
                is Action.CalendarLoading -> dispatch(Loading(action.isLoading))
                is Action.CalendarLoaded -> dispatch(CalendarUpdated(action.entries))
            }
        }

        private fun loadCalendar(forceRefresh: Boolean) {
            if (state().isLoading) return

            dispatch(Loading(true))
            dispatch(Error(null))
            val calendarUseCase = homeDependencies.getCalendarUseCase

            scope.launch {
                calendarUseCase(forceRefresh)
                    .collectLatest { result ->
                        result
                            .onSuccess { entries ->
                                dispatch(CalendarUpdated(entries))
                                dispatch(Loading(false))
                            }.onFailure { throwable ->
                                dispatch(Error(throwable.message ?: "Unknown error"))
                                dispatch(Loading(false))
                            }
                    }
                print("entries - ${state().calendarEntries}")
            }
        }
    }

    private object ReducerImpl : Reducer<HomeStore.State, Msg> {
        override fun HomeStore.State.reduce(msg: Msg): HomeStore.State {
            return when (msg) {
                is Loading -> copy(isLoading = msg.isLoading)
                is CalendarUpdated -> copy(calendarEntries = msg.entries, error = null)
                is Error -> copy(error = msg.error, isLoading = false)
            }
        }
    }
}