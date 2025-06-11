package com.f1pulse.domain

import com.f1pulse.data.local.DriverEntity
import com.f1pulse.data.repository.DriverRepository
import javax.inject.Inject

class DriverUpdateUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    suspend operator fun invoke(driver: DriverEntity, userId: String) {
        repository.bookmarkDriver(driver.driverId, driver.isBookmarked, userId)
    }
}
