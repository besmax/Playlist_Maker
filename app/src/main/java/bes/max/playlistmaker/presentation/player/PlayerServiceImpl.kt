package bes.max.playlistmaker.presentation.player

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.player.Player
import bes.max.playlistmaker.domain.player.PlayerService
import bes.max.playlistmaker.domain.player.PlayerService.Companion.EXTRA_ARTIST
import bes.max.playlistmaker.domain.player.PlayerService.Companion.EXTRA_TITLE
import bes.max.playlistmaker.domain.player.PlayerService.Companion.EXTRA_URL
import bes.max.playlistmaker.domain.player.PlayerService.Companion.NOTIFICATION_CHANNEL_ID
import bes.max.playlistmaker.domain.player.PlayerService.Companion.SERVICE_NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerServiceImpl : Service(), PlayerService {

    private val binder = PlayerServiceImplBinder()
    private var trackUrl: String? = null
    private var trackArtist: String? = null
    private var trackTitle: String? = null
    private val player: Player = get()
    override val playerState = player.playerState
    override val currentPosition = player.currentPosition
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onBind(p0: Intent?): IBinder? {
        trackUrl = p0?.getStringExtra(EXTRA_URL)
        trackArtist = p0?.getStringExtra(EXTRA_ARTIST)
        trackTitle = p0?.getStringExtra(EXTRA_TITLE)

        if (trackUrl != null) {
            player.preparePlayer(trackUrl!!)
        }

        hideNotificationOnTrackEnded()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player.releasePlayer()
        stopSelf()
        return super.onUnbind(intent)
    }

    override fun playback() {
        when (playerState.value) {

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                player.startPlayer()
            }

            PlayerState.STATE_PLAYING -> {
                player.pausePlayer()
            }

            else -> {}
        }
    }

    override fun getCurrentTime(): String {
        return formatIntToFormattedTimeText(player.getCurrentPosition())
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
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

    private fun formatIntToFormattedTimeText(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun hideNotificationOnTrackEnded() {
        coroutineScope.launch {
            playerState.collect() { state ->
                if (state != PlayerState.STATE_PLAYING || state != PlayerState.STATE_PAUSED) {
                    hideNotification()
                }
            }
        }
    }

    inner class PlayerServiceImplBinder : Binder() {
        fun getService(): PlayerServiceImpl = this@PlayerServiceImpl
    }
}