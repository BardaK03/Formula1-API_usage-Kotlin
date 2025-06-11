package com.f1pulse.domain

import com.f1pulse.data.local.DriverEntity
import com.f1pulse.data.repository.DriverRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDriversUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    operator fun invoke(): Flow<List<DriverEntity>> {
        return repository.getAllDrivers()
    }
}

class GetDriverByIdUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    operator fun invoke(id: String): Flow<DriverEntity?> {
        return repository.getDriverById(id)
    }
}

class RefreshDriversUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    suspend operator fun invoke() {
        repository.refreshDrivers()
    }
}

class BookmarkDriverUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    suspend operator fun invoke(driverId: String, isBookmarked: Boolean, userId: String) {
        repository.bookmarkDriver(driverId, isBookmarked, userId)
    }
}

class GetBookmarkedDriversUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    operator fun invoke(userId: String): Flow<List<DriverEntity>> {
        return repository.getBookmarkedDrivers(userId)
    }
}
