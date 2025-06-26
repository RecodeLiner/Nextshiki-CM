package com.rcl.nextshiki.data.repository

import com.rcl.nextshiki.data.api.CalendarRemoteDataSource
import com.rcl.nextshiki.data.local.CalendarEntryDao
import com.rcl.nextshiki.data.local.CalendarEntryEntity
import com.rcl.nextshiki.data.mappers.toDomain
import com.rcl.nextshiki.data.mappers.toEntity
import com.rcl.nextshiki.domain.models.CalendarEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class CalendarRepositoryImpl(
    private val remoteDataSource: CalendarRemoteDataSource,
    private val localDataSource: CalendarEntryDao
) : CalendarRepository {
    override fun getCalendarEntries(forceRefresh: Boolean): Flow<Result<List<CalendarEntry>>> =
        flow {
            val initialLocalData = localDataSource.getAllCalendarEntries().first()
            emit(Result.success(initialLocalData.map { it.toDomain() }))

            localDataSource.getAllCalendarEntries()
                .map { entities -> Result.success(entities.map { it.toDomain() }) }
                .collect { result ->
                    emit(result)
                }
        }
    @OptIn(ExperimentalTime::class)
    override suspend fun refreshCalendarEntries(): Result<Unit> {
        return try {
            val remoteDataResult = remoteDataSource.getCalendar()
            if (remoteDataResult.isSuccess) {
                val remoteEntries = remoteDataResult.getOrThrow().map { it.toDomain() }
                println("refreshCalendarEntries: Successfully fetched ${remoteEntries.size} remote entries.")

                val existingLocalEntries = localDataSource.getAllCalendarEntries().first().map { it.toDomain() }
                println("refreshCalendarEntries: Found ${existingLocalEntries.size} existing local entries.")

                val newEntries = mutableListOf<CalendarEntryEntity>()
                val updatedEntries = mutableListOf<CalendarEntryEntity>()
                val entriesToRemove = mutableListOf<Long>()

                val remoteEntryUniqueKeys = remoteEntries.associateBy {
                    it.nextEpisodeAt.toString() + Json.encodeToString(it.anime)
                }
                val localEntryUniqueKeys = existingLocalEntries.associateBy {
                    it.nextEpisodeAt.toString() + Json.encodeToString(it.anime)
                }

                localEntryUniqueKeys.forEach { (uniqueKey, localEntry) ->
                    if (!remoteEntryUniqueKeys.containsKey(uniqueKey)) {
                        val existingEntity = localDataSource.getCalendarEntryByUniqueFields(
                            localEntry.nextEpisodeAt.toString(),
                            Json.encodeToString(localEntry.anime)
                        )
                        existingEntity?.let { entriesToRemove.add(it.id) }
                    }
                }

                remoteEntryUniqueKeys.forEach { (uniqueKey, remoteEntry) ->
                    val existingLocalEntry = localEntryUniqueKeys[uniqueKey]

                    if (existingLocalEntry == null) {
                        newEntries.add(remoteEntry.toEntity())
                    } else {
                        val existingEntityFromDb = localDataSource.getCalendarEntryByUniqueFields(
                            existingLocalEntry.nextEpisodeAt.toString(),
                            Json.encodeToString(existingLocalEntry.anime)
                        )
                        val updatedEntity = remoteEntry.toEntity(id = existingEntityFromDb?.id ?: 0L)

                        if (existingEntityFromDb != null && updatedEntity != existingEntityFromDb) {
                            updatedEntries.add(updatedEntity)
                        }
                    }
                }

                if (entriesToRemove.isNotEmpty()) {
                    localDataSource.deleteByIds(entriesToRemove)
                    println("refreshCalendarEntries: Deleted ${entriesToRemove.size} entries from local DB.")
                }
                if (newEntries.isNotEmpty()) {
                    localDataSource.insertAll(newEntries)
                    println("refreshCalendarEntries: Inserted ${newEntries.size} new entries into local DB.")
                }
                updatedEntries.forEach {
                    localDataSource.update(it)
                    println("refreshCalendarEntries: Updated entry with ID: ${it.id}")
                }

                Result.success(Unit)
            } else {
                println("refreshCalendarEntries: Failed to fetch remote data: ${remoteDataResult.exceptionOrNull()?.message}")
                Result.failure(
                    remoteDataResult.exceptionOrNull() ?: Exception("Failed to fetch remote data")
                )
            }
        } catch (e: Exception) {
            println("refreshCalendarEntries: Caught exception: ${e.message}")
            Result.failure(e)
        }
    }
}
