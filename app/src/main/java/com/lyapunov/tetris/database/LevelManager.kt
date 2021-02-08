package com.lyapunov.tetris.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class LevelManager (context: Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "INIT_LEVEL")
    companion object {
        val INITIAL_LEVEL = intPreferencesKey("init_level_key")
    }
    suspend fun setInitalLevel(level: Int) {
        dataStore.edit { preferences: MutablePreferences -> preferences[INITIAL_LEVEL] = level }
    }

    suspend fun getInitialLevel(): Int? {
        return dataStore.data.first()[INITIAL_LEVEL]
    }
}