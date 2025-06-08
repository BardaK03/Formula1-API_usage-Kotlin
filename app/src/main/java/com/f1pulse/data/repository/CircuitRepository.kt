package com.f1pulse.data.repository

import com.f1pulse.data.local.CircuitEntity
import kotlinx.coroutines.flow.Flow

interface CircuitRepository {
    fun getAllCircuits(): Flow<List<CircuitEntity>>
    fun getCircuitById(id: String): Flow<CircuitEntity?>
    suspend fun refreshCircuits()
}
