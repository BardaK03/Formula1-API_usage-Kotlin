package com.f1pulse.data.repository

import com.f1pulse.data.local.CircuitDao
import com.f1pulse.data.local.CircuitEntity
import com.f1pulse.data.remote.F1ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CircuitRepositoryImpl @Inject constructor(
    private val circuitDao: CircuitDao,
    private val apiService: F1ApiService
) : CircuitRepository {

    override fun getAllCircuits(): Flow<List<CircuitEntity>> {
        return circuitDao.getAllCircuits()
    }

    override fun getCircuitById(id: String): Flow<CircuitEntity?> {
        return circuitDao.getCircuitById(id)
    }

    override suspend fun refreshCircuits() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCircuits()
                if (response.success && response.data.isNotEmpty()) {
                    val circuits = response.data.map { dto ->
                        CircuitEntity(
                            circuitId = dto.circuitId,
                            circuitName = dto.circuitName,
                            url = dto.url,
                            locality = dto.location?.locality,
                            country = dto.location?.country,
                            lat = dto.location?.lat,
                            longitude = dto.location?.long  // Using 'long' from LocationDto
                        )
                    }
                    circuitDao.insertCircuits(circuits)
                    println("DEBUG: Successfully inserted ${circuits.size} circuits into database")
                } else {
                    println("DEBUG: API returned unsuccessful response or empty data: ${response.message}")
                }
            } catch (e: Exception) {
                // Log the error for debugging
                println("DEBUG: Error refreshing circuits: ${e.message}")
                e.printStackTrace()
                throw e // Re-throw to propagate to the ViewModel
            }
        }
    }
}
