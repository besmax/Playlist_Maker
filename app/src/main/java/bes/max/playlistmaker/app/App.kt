package bes.max.playlistmaker.app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import bes.max.playlistmaker.data.settings.SettingsDaoImpl
import bes.max.playlistmaker.di.DataStoreModule
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        DataStoreModule.providePreferencesDataStore(applicationContext)

        val settingsDao = SettingsDaoImpl(this)
        kotlinx.coroutines.MainScope().launch {
            Log.d("AppClass", "isNightModeActiveDefault returns: ${settingsDao.isNightModeActive()}")
            settingsDao.isNightModeActive().asLiveData().observeForever { switchTheme(it) }
        }

    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO

        )

    }
}