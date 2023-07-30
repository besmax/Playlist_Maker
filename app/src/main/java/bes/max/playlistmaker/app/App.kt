package bes.max.playlistmaker.app

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

const val SETTINGS_PREFERENCES = "settings_preferences"
const val DARK_THEME_PREFERENCES_KEY = "dark_theme_preference_key"
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //getting value if night mode is active on device for case when we do not have saved preference
        val isNightModeActiveDefault = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resources.configuration.isNightModeActive
        } else {
            false
        }

        val darkThemePreference =
            getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        val darkTheme = darkThemePreference.getBoolean(
            DARK_THEME_PREFERENCES_KEY, isNightModeActiveDefault
        )
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO

        )

    }
}