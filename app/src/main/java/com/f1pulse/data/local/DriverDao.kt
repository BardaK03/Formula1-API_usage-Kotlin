package com.f1pulse.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE driverId = :driverId LIMIT 1")
    fun getDriverById(driverId: String): Flow<DriverEntity?>

    @Query("SELECT * FROM drivers WHERE driverId = :driverId LIMIT 1")
    suspend fun getDriverByIdSync(driverId: String): DriverEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverEntity>)

    @Update
    suspend fun updateDriver(driver: DriverEntity)

    @Query("SELECT * FROM drivers WHERE isBookmarked = 1")
    fun getBookmarkedDrivers(): Flow<List<DriverEntity>>
}
