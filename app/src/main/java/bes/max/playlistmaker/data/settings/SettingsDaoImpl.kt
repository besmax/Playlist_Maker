package bes.max.playlistmaker.data.settings

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsDaoImpl(
    private val context: Context,
    private val preferencesDataStore: DataStore<Preferences>
) : SettingsDao {

    override fun isNightModeActive(): Flow<Boolean> {
        return preferencesDataStore.data
            .catch { exception ->
                Log.e(
                    TAG,
                    "Error during getting DataStore: ${exception.toString()}"
                )
            }
            .map { preferences ->
                preferences[DARK_THEME_PREFERENCES_KEY] ?: isNightModeActiveDefault()
            }
    }

    override suspend fun setIsNightModeActive(isNightModeActive: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[DARK_THEME_PREFERENCES_KEY] = isNightModeActive
        }
    }

    private fun isNightModeActiveDefault(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.resources.configuration.isNightModeActive
        } else {
            false
        }
    }

    companion object {
        private const val TAG = "SettingsDaoImpl"
        private val DARK_THEME_PREFERENCES_KEY = booleanPreferencesKey("dark_theme_preference_key")
    }

}