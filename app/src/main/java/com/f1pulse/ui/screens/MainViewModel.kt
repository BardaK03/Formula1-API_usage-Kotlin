package com.f1pulse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1pulse.data.local.CircuitEntity
import com.f1pulse.data.local.DriverEntity
import com.f1pulse.domain.GetAllCircuitsUseCase
import com.f1pulse.domain.GetBookmarkedDriversUseCase
import com.f1pulse.domain.RefreshCircuitsUseCase
import com.f1pulse.domain.DriverUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCircuitsUseCase: GetAllCircuitsUseCase,
    private val getBookmarkedDriversUseCase: GetBookmarkedDriversUseCase,
    private val updateDriverUseCase: DriverUpdateUseCase,
    private val refreshCircuitsUseCase: RefreshCircuitsUseCase
) : ViewModel() {
    private val _circuits = MutableStateFlow<List<CircuitEntity>>(emptyList())
    val circuits: StateFlow<List<CircuitEntity>> = _circuits.asStateFlow()

    private val _bookmarks = MutableStateFlow<List<DriverEntity>>(emptyList())
    val bookmarks: StateFlow<List<DriverEntity>> = _bookmarks.asStateFlow()

    init {
        getCircuits()
    }

    private fun getCircuits() {
        getCircuitsUseCase().onEach { circuitList ->
            _circuits.value = circuitList
            if (circuitList.isEmpty()) {
                refreshCircuits()
            }
        }.launchIn(viewModelScope)
    }

    fun getBookmarks(userId: String) {
        getBookmarkedDriversUseCase(userId).onEach { bookmarkList ->
            _bookmarks.value = bookmarkList
        }.launchIn(viewModelScope)
    }

    fun refreshCircuits() {
        viewModelScope.launch {
            try {
                refreshCircuitsUseCase()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun unbookmark(driver: DriverEntity, userId: String) {
        viewModelScope.launch {
            updateDriverUseCase(driver.copy(isBookmarked = false), userId)
        }
    }
}
