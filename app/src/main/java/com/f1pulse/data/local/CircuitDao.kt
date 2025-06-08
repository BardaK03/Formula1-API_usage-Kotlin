package com.f1pulse.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CircuitDao {
    @Query("SELECT * FROM circuits")
    fun getAllCircuits(): Flow<List<CircuitEntity>>

    @Query("SELECT * FROM circuits WHERE circuitId = :circuitId LIMIT 1")
    fun getCircuitById(circuitId: String): Flow<CircuitEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCircuits(circuits: List<CircuitEntity>)
}
