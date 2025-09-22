package com.coursecampus.athleteconnect.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

object ThemePreferenceManager {
    private val THEME_KEY = stringPreferencesKey("theme_mode")

    fun getThemeMode(context: Context): Flow<String> =
        context.themeDataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: "system"
        }

    suspend fun setThemeMode(context: Context, mode: String) {
        context.themeDataStore.edit { prefs ->
            prefs[THEME_KEY] = mode
        }
    }
}

