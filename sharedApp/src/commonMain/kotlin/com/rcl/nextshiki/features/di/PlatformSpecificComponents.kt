package com.rcl.nextshiki.features.di

import androidx.room.RoomDatabase
import com.rcl.nextshiki.data.local.AppDatabase
import com.rcl.nextshiki.features.di.modules.DataStoreBuilder
import io.ktor.client.HttpClient

abstract class PlatformSpecificComponents {
    abstract val roomDatabase: RoomDatabase.Builder<AppDatabase>
    abstract val dataStoreBuilder: DataStoreBuilder
    abstract val httpClient: HttpClient
}