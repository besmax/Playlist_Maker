package bes.max.playlistmaker.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import bes.max.playlistmaker.R

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val darkThemePreference =
            getSharedPreferences(getString(R.string.settings_preferences), Context.MODE_PRIVATE)
        val darkTheme = darkThemePreference.getBoolean(
            getString(R.string.dark_theme_preference_key), resources.configuration.isNightModeActive
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