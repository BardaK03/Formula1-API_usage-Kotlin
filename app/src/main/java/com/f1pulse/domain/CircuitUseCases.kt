package com.f1pulse.domain

import com.f1pulse.data.local.CircuitEntity
import com.f1pulse.data.repository.CircuitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCircuitsUseCase @Inject constructor(
    private val repository: CircuitRepository
) {
    operator fun invoke(): Flow<List<CircuitEntity>> {
        return repository.getAllCircuits()
    }
}

class GetCircuitByIdUseCase @Inject constructor(
    private val repository: CircuitRepository
) {
    operator fun invoke(id: String): Flow<CircuitEntity?> {
        return repository.getCircuitById(id)
    }
}

class RefreshCircuitsUseCase @Inject constructor(
    private val repository: CircuitRepository
) {
    suspend operator fun invoke() {
        repository.refreshCircuits()
    }
}
