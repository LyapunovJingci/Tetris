package com.lyapunov.tetris.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class BlockThemeManager(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "BLOCK_THEME")
    companion object {
        val BLOCK_THEME_NAME = stringPreferencesKey("block_theme_key")
    }
    suspend fun setTheme(themeName: String) {
        dataStore.edit { preferences: MutablePreferences -> preferences[BLOCK_THEME_NAME] = themeName }
    }

    suspend fun getTheme(): String? {
        return dataStore.data.first()[BLOCK_THEME_NAME]
    }
}