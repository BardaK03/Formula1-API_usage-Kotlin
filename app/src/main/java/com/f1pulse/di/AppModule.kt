package com.f1pulse.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.f1pulse.F1PulseApp
import com.f1pulse.data.local.F1Database
import com.f1pulse.data.remote.F1ApiService
import com.f1pulse.data.repository.CircuitRepository
import com.f1pulse.data.repository.CircuitRepositoryImpl
import com.f1pulse.data.repository.DriverRepository
import com.f1pulse.data.repository.DriverRepositoryImpl
import com.f1pulse.utils.userPreferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideF1Database(@ApplicationContext context: Context): F1Database {
        return Room.databaseBuilder(
            context,
            F1Database::class.java,
            F1Database.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDriverDao(database: F1Database) = database.driverDao()

    @Provides
    @Singleton
    fun provideCircuitDao(database: F1Database) = database.circuitDao()

    @Provides
    @Singleton
    fun provideF1ApiService(): F1ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca/ergast/")  // Using Jolpi.ca F1 API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDriverRepository(
        driverDao: com.f1pulse.data.local.DriverDao,
        apiService: F1ApiService
    ): DriverRepository {
        return DriverRepositoryImpl(driverDao, apiService)
    }

    @Provides
    @Singleton
    fun provideCircuitRepository(
        circuitDao: com.f1pulse.data.local.CircuitDao,
        apiService: F1ApiService
    ): CircuitRepository {
        return CircuitRepositoryImpl(circuitDao, apiService)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> = context.userPreferencesDataStore

    @Provides
    @Singleton
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth {
        try {
            val auth = FirebaseAuth.getInstance()
            // Disable automatic reCAPTCHA verification
            auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            Log.d("AppModule", "Firebase Auth initialized with reCAPTCHA verification disabled")
            return auth
        } catch (e: Exception) {
            Log.e("AppModule", "Error getting FirebaseAuth instance: ${e.message}")
            throw e
        }
    }
}
