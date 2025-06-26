package com.rcl.nextshiki.features.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

class DataStoreBuilder(
    private val path: String
) {
    fun build() : DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { path.toPath() }
        )
    }
    companion object {
        const val DATA_STORE_FILE_NAME = "settings.preferences_pb"
    }
}