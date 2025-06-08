package com.f1pulse.data.repository

import com.f1pulse.data.local.CircuitEntity
import com.f1pulse.data.local.DriverEntity
import kotlinx.coroutines.flow.Flow

interface F1Repository {
    fun getDrivers(): Flow<List<DriverEntity>>
    fun getCircuits(): Flow<List<CircuitEntity>>
    fun getBookmarkedDrivers(): Flow<List<DriverEntity>>
    suspend fun updateDriver(driver: DriverEntity)
    suspend fun getDriverById(id: String): DriverEntity?
}

