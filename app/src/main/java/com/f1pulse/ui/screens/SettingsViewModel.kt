package com.f1pulse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1pulse.utils.SettingsDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val isDarkTheme = settingsDataStore.isDarkTheme
    val displayName = settingsDataStore.displayName
    // Keeping lastTab even though it's not currently used - might be needed in future
    val lastTab = settingsDataStore.lastTab

    private val _isLoading = MutableStateFlow(false)
    // Keeping isLoading exposed for UI state management
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkTheme(isDark)
        }
    }

    fun setDisplayName(name: String) {
        viewModelScope.launch {
            settingsDataStore.setDisplayName(name)
        }
    }

    // Keeping setLastTab for future use cases
    fun setLastTab(tab: String) {
        viewModelScope.launch {
            settingsDataStore.setLastTab(tab)
        }
    }

    fun syncDisplayNameToFirebase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = firebaseAuth.currentUser
                // Get the displayName value without indefinite collection
                val name = displayName.first()

                if (user != null && name.isNotEmpty()) {
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            _error.value = task.exception?.message
                        }
                        _isLoading.value = false
                    }
                } else {
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Error handling function
    fun clearError() {
        _error.value = null
    }

    // Authentication function
    fun logout() {
        viewModelScope.launch {
            firebaseAuth.signOut()
            settingsDataStore.clearPreferences()
        }
    }
}
