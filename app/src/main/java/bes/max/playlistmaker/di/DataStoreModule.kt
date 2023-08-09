package bes.max.playlistmaker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_PREFERENCES = "settings_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES
)

object DataStoreModule {

    var preferencesDataStore: DataStore<Preferences>? = null

    fun providePreferencesDataStore(appContext: Context) {
        preferencesDataStore = appContext.dataStore
    }
}