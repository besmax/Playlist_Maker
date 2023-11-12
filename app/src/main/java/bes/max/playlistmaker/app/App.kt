package bes.max.playlistmaker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import bes.max.playlistmaker.data.settings.SettingsDao
import bes.max.playlistmaker.di.dataModule
import bes.max.playlistmaker.di.domainModule
import bes.max.playlistmaker.di.repositoryModule
import bes.max.playlistmaker.di.viewModelModule
import bes.max.playlistmaker.domain.player.PlaybackService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule, repositoryModule)
        }

        setUpNotificationManager()

        val settingsDao: SettingsDao = getKoin().get()
        kotlinx.coroutines.MainScope().launch {
            Log.d(
                "AppClass",
                "isNightModeActiveDefault returns: ${settingsDao.isNightModeActive()}"
            )
            settingsDao.isNightModeActive().asLiveData().observeForever { switchTheme(it) }
        }

    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO

        )
    }

    private fun setUpNotificationManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PlaybackService.CHANNEL_ID,
                "Music Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}