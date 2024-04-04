package bes.max.playlistmaker.presentation.player

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import bes.max.playlistmaker.R
import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.domain.player.Player
import bes.max.playlistmaker.presentation.player.PlayerService.Companion.EXTRA_ARTIST
import bes.max.playlistmaker.presentation.player.PlayerService.Companion.EXTRA_TITLE
import bes.max.playlistmaker.presentation.player.PlayerService.Companion.EXTRA_URL
import bes.max.playlistmaker.presentation.player.PlayerService.Companion.NOTIFICATION_CHANNEL_ID
import bes.max.playlistmaker.presentation.player.PlayerService.Companion.SERVICE_NOTIFICATION_ID

class PlayerServiceImpl : Service(), PlayerService {

    private val binder = PlayerServiceImplBinder()
    private var trackUrl: String? = null
    private var trackArtist: String? = null
    private var trackTitle: String? = null


    private val player: Player = PlayerImpl(
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    )

    override fun onCreate() {
        super.onCreate()

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        trackUrl = p0?.getStringExtra(EXTRA_URL)
        trackArtist = p0?.getStringExtra(EXTRA_ARTIST)
        trackTitle = p0?.getStringExtra(EXTRA_TITLE)

        if (trackUrl != null) {
            player.preparePlayer(trackUrl!!)
        }
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player.releasePlayer()
        return super.onUnbind(intent)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_placeholder, trackArtist, trackTitle))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    inner class PlayerServiceImplBinder : Binder() {
        fun getService(): PlayerServiceImpl = this@PlayerServiceImpl
    }
}