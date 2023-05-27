package bes.max.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val darkThemePreference = getSharedPreferences(getString(R.string.settings_preferences), Context.MODE_PRIVATE)
        darkTheme = darkThemePreference.getBoolean(
            getString(R.string.dark_theme_preference_key), resources.configuration.isNightModeActive
        )
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}