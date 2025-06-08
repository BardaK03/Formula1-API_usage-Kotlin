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
                println("DEBUG: Attempting to fetch circuits from API")
                val response = apiService.getCircuits()
                println("DEBUG: Raw API response received - MRData structure available: ${response.mrData != null}")

                // Extract circuits from the nested structure
                val circuitsTable = response.mrData.circuitTable
                if (circuitsTable != null && circuitsTable.circuits != null && circuitsTable.circuits.isNotEmpty()) {
                    val circuitsList = circuitsTable.circuits
                    println("DEBUG: Successfully received ${circuitsList.size} circuits")

                    val circuits = circuitsList.map { circuit ->
                        println("DEBUG: Processing circuit: ${circuit.circuitName}")
                        CircuitEntity(
                            circuitId = circuit.circuitId,
                            circuitName = circuit.circuitName,
                            url = circuit.url,
                            locality = circuit.location?.locality,
                            country = circuit.location?.country,
                            lat = circuit.location?.lat,
                            longitude = circuit.location?.long
                        )
                    }
                    println("DEBUG: Mapped ${circuits.size} circuits to entities")
                    circuitDao.insertCircuits(circuits)
                    println("DEBUG: Successfully inserted ${circuits.size} circuits into database")
                } else {
                    println("DEBUG: API returned empty circuit data")
                    println("DEBUG: MRData structure: ${response.mrData}")
                }
            } catch (e: Exception) {
                // Log the error for debugging
                println("DEBUG: Error refreshing circuits: ${e.message}")
                println("DEBUG: Exception type: ${e.javaClass.name}")
                e.printStackTrace()
                throw e // Re-throw to propagate to the ViewModel
            }
        }
    }
}
