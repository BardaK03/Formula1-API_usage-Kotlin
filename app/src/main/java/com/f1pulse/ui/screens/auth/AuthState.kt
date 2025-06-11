package com.f1pulse.ui.screens.auth

/**
 * Represents the various states of authentication in the app.
 */
sealed class AuthState {
    /**
     * Initial state when the app starts, before checking auth status
     */
    object Initial : AuthState()

    /**
     * Authentication is in progress (e.g., login or registration request is being processed)
     */
    object Loading : AuthState()

    /**
     * User is not authenticated
     */
    object Unauthenticated : AuthState()

    /**
     * User is authenticated
     * @param displayName The display name of the authenticated user
     */
    data class Authenticated(val displayName: String) : AuthState()

    /**
     * An error occurred during authentication
     * @param message The error message
     */
    data class Error(val message: String) : AuthState()
}
