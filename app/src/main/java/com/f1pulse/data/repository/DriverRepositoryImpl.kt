package com.f1pulse.data.repository

import com.f1pulse.data.local.DriverDao
import com.f1pulse.data.local.DriverEntity
import com.f1pulse.data.remote.F1ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val driverDao: DriverDao,
    private val apiService: F1ApiService
) : DriverRepository {

    override fun getAllDrivers(): Flow<List<DriverEntity>> {
        return driverDao.getAllDrivers()
    }

    override fun getDriverById(id: String): Flow<DriverEntity?> {
        return driverDao.getDriverById(id)
    }

    override fun getBookmarkedDrivers(userId: String): Flow<List<DriverEntity>> {
        return driverDao.getBookmarkedDrivers(userId)
    }

    override suspend fun refreshDrivers() {
        withContext(Dispatchers.IO) {
            try {
                println("DEBUG: Attempting to fetch drivers from API")
                val response = apiService.getDrivers()
                println("DEBUG: Raw API response received - MRData structure available: ${response.mrData != null}")

                // Extract drivers from the nested structure
                val driversTable = response.mrData.driverTable
                if (driversTable != null && driversTable.drivers != null && driversTable.drivers.isNotEmpty()) {
                    val driversList = driversTable.drivers
                    println("DEBUG: Successfully received ${driversList.size} drivers")

                    val drivers = driversList.map { driver ->
                        println("DEBUG: Processing driver: ${driver.givenName} ${driver.familyName}")
                        DriverEntity(
                            driverId = driver.driverId,
                            code = driver.code ?: "",
                            dateOfBirth = driver.dateOfBirth ?: "",
                            familyName = driver.familyName ?: "",
                            givenName = driver.givenName ?: "",
                            nationality = driver.nationality ?: "",
                            permanentNumber = driver.permanentNumber ?: "",
                            url = driver.url ?: "",
                            team = "", // We don't have team info in this response
                            isBookmarked = false,
                            userId = "" // Set to empty or default, must be set properly when bookmarking
                        )
                    }
                    println("DEBUG: Mapped ${drivers.size} drivers to entities")
                    driverDao.insertDrivers(drivers)
                    println("DEBUG: Successfully inserted ${drivers.size} drivers into database")
                } else {
                    println("DEBUG: API returned empty driver data")
                    println("DEBUG: MRData structure: ${response.mrData}")
                }
            } catch (e: Exception) {
                // Log the error for debugging
                println("DEBUG: Error refreshing drivers: ${e.message}")
                println("DEBUG: Exception type: ${e.javaClass.name}")
                e.printStackTrace()
                throw e // Re-throw to propagate to the ViewModel
            }
        }
    }

    override suspend fun bookmarkDriver(driverId: String, isBookmarked: Boolean, userId: String) {
        withContext(Dispatchers.IO) {
            val driver = driverDao.getDriverByIdSync(driverId) ?: return@withContext
            driverDao.updateDriver(driver.copy(isBookmarked = isBookmarked, userId = userId))
        }
    }
}
