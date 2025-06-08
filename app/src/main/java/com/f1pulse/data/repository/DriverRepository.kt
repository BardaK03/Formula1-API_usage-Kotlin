package com.f1pulse.data.repository

import com.f1pulse.data.local.DriverEntity
import kotlinx.coroutines.flow.Flow

interface DriverRepository {
    fun getAllDrivers(): Flow<List<DriverEntity>>
    fun getDriverById(id: String): Flow<DriverEntity?>
    fun getBookmarkedDrivers(): Flow<List<DriverEntity>>
    suspend fun refreshDrivers()
    suspend fun bookmarkDriver(driverId: String, isBookmarked: Boolean)
}
