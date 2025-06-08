package com.f1pulse.domain

import com.f1pulse.data.local.CircuitEntity
import com.f1pulse.data.local.DriverEntity
import com.f1pulse.data.repository.F1Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Legacy use case collection - consider migrating to specific repository implementations
 * (DriverRepository and CircuitRepository)
 */
data class F1UseCases(
    val getDrivers: GetF1DriversUseCase,
    val getCircuits: GetF1CircuitsUseCase
)

class GetF1DriversUseCase @Inject constructor(
    private val repository: F1Repository
) {
    operator fun invoke(): Flow<List<DriverEntity>> = repository.getDrivers()
}

class GetF1CircuitsUseCase @Inject constructor(
    private val repository: F1Repository
) {
    operator fun invoke(): Flow<List<CircuitEntity>> = repository.getCircuits()
}
