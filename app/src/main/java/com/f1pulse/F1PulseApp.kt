package com.f1pulse

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "F1PulseApp"

@HiltAndroidApp
class F1PulseApp : Application() {

    companion object {
        private var initialized = false
        private var instance: F1PulseApp? = null

        fun getInstance(): F1PulseApp {
            return instance!!
        }

        @Synchronized
        fun initializeFirebase(application: Application) {
            if (!initialized) {
                try {
                    if (FirebaseApp.getApps(application).isEmpty()) {
                        FirebaseApp.initializeApp(application)
                        Log.d(TAG, "Firebase initialized successfully")
                    } else {
                        Log.d(TAG, "Firebase was already initialized")
                    }
                    initialized = true
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to initialize Firebase: ${e.message}", e)
                    throw e // Rethrow to make initialization failures visible
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeFirebase(this)
    }
}
