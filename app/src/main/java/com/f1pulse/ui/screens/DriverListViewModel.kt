package com.f1pulse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1pulse.data.local.DriverEntity
import com.f1pulse.domain.BookmarkDriverUseCase
import com.f1pulse.domain.GetAllDriversUseCase
import com.f1pulse.domain.GetDriverByIdUseCase
import com.f1pulse.domain.RefreshDriversUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverListViewModel @Inject constructor(
    private val getAllDriversUseCase: GetAllDriversUseCase,
    private val getDriverByIdUseCase: GetDriverByIdUseCase,
    private val refreshDriversUseCase: RefreshDriversUseCase,
    private val bookmarkDriverUseCase: BookmarkDriverUseCase
) : ViewModel() {

    private val _drivers = MutableStateFlow<List<DriverEntity>>(emptyList())
    val drivers: StateFlow<List<DriverEntity>> = _drivers.asStateFlow()

    private val _currentDriver = MutableStateFlow<DriverEntity?>(null)
    val currentDriver: StateFlow<DriverEntity?> = _currentDriver.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadDrivers()
    }

    fun loadDrivers() {
        viewModelScope.launch {
            getAllDriversUseCase().collect { driversList ->
                _drivers.value = driversList
                if (driversList.isEmpty()) {
                    refreshDrivers()
                }
            }
        }
    }

    fun loadDriverDetails(driverId: String) {
        viewModelScope.launch {
            getDriverByIdUseCase(driverId).collect { driver ->
                _currentDriver.value = driver
            }
        }
    }

    fun refreshDrivers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                refreshDriversUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleBookmark(driverId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            try {
                bookmarkDriverUseCase(driverId, isBookmarked)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
