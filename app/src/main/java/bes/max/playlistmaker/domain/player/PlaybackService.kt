package bes.max.playlistmaker.domain.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.PlayerState
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class PlaybackService() : Service() {

    private val playerInteractor: PlayerInteractor by inject()
    private val context: Context by inject()

    val state = playerInteractor.state

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataSourceUrl = intent?.getStringExtra(DATA_SOURCE_URL)

        when (intent?.action) {
            PlaybackServiceActions.PLAY.toString() -> play(dataSourceUrl)
            PlaybackServiceActions.PAUSE.toString() -> pause()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun releasePlayer() {
        playerInteractor.release()
    }

    fun getCurrentTime() = playerInteractor.getCurrentTime()


    fun preparePlayer(dataSourceUrl: String) {
        playerInteractor.preparePlayer(dataSourceUrl)
    }

    fun playbackControl(dataSourceUrl: String? = null) {
        when (playerInteractor.state.value) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                val intent = Intent(context, PlaybackService::class.java)
                intent.setAction(PlaybackServiceActions.PLAY.toString())
                context.startForegroundService(intent)

            }

            PlayerState.STATE_DEFAULT -> {
                if (dataSourceUrl != null) {
                    val intent = Intent(context, PlaybackService::class.java)
                    intent.setAction(PlaybackServiceActions.PLAY.toString())
                    intent.putExtra(DATA_SOURCE_URL, dataSourceUrl)
                    context.startForegroundService(intent)
                }
            }

            PlayerState.STATE_PLAYING -> {
                val intent = Intent(context, PlaybackService::class.java)
                intent.setAction(PlaybackServiceActions.PAUSE.toString())
                context.startForegroundService(intent)

            }
        }
    }


    private fun play(dataSourceUrl: String?) {
        when (playerInteractor.state.value) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                playerInteractor.play()
                startNotification()
            }

            PlayerState.STATE_DEFAULT -> {
                if (!dataSourceUrl.isNullOrBlank()) {
                    playerInteractor.preparePlayer(dataSourceUrl)
                    playerInteractor.play()
                    startNotification()
                }
            }

            PlayerState.STATE_PLAYING -> {
//                if (!dataSourceUrl.isNullOrBlank()) {
//                    playerInteractor.release()
////                    playerInteractor.preparePlayer(dataSourceUrl)
////                    playerInteractor.play()
//                }
            }

        }
    }

    private fun startNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_1_foreground)
            .setContentTitle("Playing track") //TODO change
            .build()
        startForeground(1, notification)
    }

    private fun pause() {
        if (playerInteractor.state.value == PlayerState.STATE_PLAYING) {
            playerInteractor.pause()
        }
    }

    companion object {
        const val DATA_SOURCE_URL = "dataSourceUrl"
        const val CHANNEL_ID = "music_channel"

        enum class PlaybackServiceActions {
            PLAY, PAUSE
        }
    }
}