package com.f1pulse.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

@Singleton
class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val DISPLAY_NAME = stringPreferencesKey("display_name")
        private val LAST_TAB = stringPreferencesKey("last_tab")
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: false
    }

    val displayName: Flow<String> = dataStore.data.map { preferences ->
        preferences[DISPLAY_NAME] ?: ""
    }

    val lastTab: Flow<String> = dataStore.data.map { preferences ->
        preferences[LAST_TAB] ?: "home"
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }

    suspend fun setDisplayName(name: String) {
        dataStore.edit { preferences ->
            preferences[DISPLAY_NAME] = name
        }
    }

    suspend fun setLastTab(tab: String) {
        dataStore.edit { preferences ->
            preferences[LAST_TAB] = tab
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
