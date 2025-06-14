package com.f1pulse.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1pulse.utils.SettingsDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        _authState.value = if (firebaseAuth.currentUser != null) {
            AuthState.Authenticated(firebaseAuth.currentUser!!.displayName ?: "")
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    val displayName = user.displayName ?: ""
                    settingsDataStore.setDisplayName(displayName)
                    _authState.value = AuthState.Authenticated(displayName)
                } else {
                    _authState.value = AuthState.Error("Authentication failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login error: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            }
        }
    }

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d(TAG, "Registering user: $email")

                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    // Update user profile with display name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                    user.updateProfile(profileUpdates).await()

                    // Save display name to DataStore
                    settingsDataStore.setDisplayName(displayName)

                    _authState.value = AuthState.Authenticated(displayName)
                } else {
                    _authState.value = AuthState.Error("Registration failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Registration error: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            firebaseAuth.signOut()
            settingsDataStore.setDisplayName("")
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}
