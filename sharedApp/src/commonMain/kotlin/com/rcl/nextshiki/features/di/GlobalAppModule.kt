package com.rcl.nextshiki.features.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rcl.nextshiki.data.api.AuthorizedHttpClient
import com.rcl.nextshiki.data.api.NSHttpClient
import com.rcl.nextshiki.data.api.TokenManager
import com.rcl.nextshiki.data.local.AppDatabase
import com.rcl.nextshiki.data.local.CalendarEntryDao
import com.rcl.nextshiki.features.di.modules.ThemeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class GlobalAppModule(
    private val platformComponent: PlatformSpecificComponents
) {
    private val dataStore: DataStore<Preferences> by lazy {
        platformComponent.dataStoreBuilder.build()
    }

    private val database: AppDatabase by lazy {
        platformComponent.roomDatabase
            .fallbackToDestructiveMigrationOnDowngrade(false)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    val tokenManager: TokenManager by lazy {
        TokenManager(
            dataStore = dataStore,
            unauthenticatedHttpClient = platformComponent.httpClient
        )
    }

    val authorizedHttpClient: AuthorizedHttpClient by lazy {
        NSHttpClient(
            httpClient = platformComponent.httpClient,
            tokenManager = tokenManager
        )
    }

    val themeColor: ThemeColor by lazy {
        ThemeColor(dataStore)
    }

    val calendarEntryDao: CalendarEntryDao by lazy {
        database.calendarEntryDao()
    }
}