package bes.max.playlistmaker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import bes.max.playlistmaker.R
import bes.max.playlistmaker.data.settings.SettingsDao
import bes.max.playlistmaker.di.dataModule
import bes.max.playlistmaker.di.domainModule
import bes.max.playlistmaker.di.repositoryModule
import bes.max.playlistmaker.di.viewModelModule
import bes.max.playlistmaker.domain.player.PlayerService
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

        val settingsDao: SettingsDao = getKoin().get()
        kotlinx.coroutines.MainScope().launch {
            settingsDao.isNightModeActive().asLiveData().observeForever { switchTheme(it) }
        }

        createPlayerNotificationChannel()
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO

        )
    }

    private fun createPlayerNotificationChannel() {
        val channel = NotificationChannel(
            PlayerService.NOTIFICATION_CHANNEL_ID,
            getString(R.string.player_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}