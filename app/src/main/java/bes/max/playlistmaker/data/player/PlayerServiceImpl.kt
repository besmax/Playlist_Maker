package bes.max.playlistmaker.data.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.player.PlaybackService
import bes.max.playlistmaker.domain.player.Player
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.UUID.randomUUID

class PlayerServiceImpl : MediaSessionService(), Player {

    private var player: androidx.media3.common.Player? = null
    private var mediaSession: MediaSession? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.STATE_DEFAULT)
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private val context: Context by inject()

    init {
        player = ExoPlayer.Builder(context).build()

        mediaSession = MediaSession.Builder(context, player!!).setId(randomUUID().toString()).build()

        val sessionToken =
            SessionToken(context, ComponentName(context, PlayerServiceImpl::class.java))
        controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture!!.addListener({
            player = controllerFuture!!.get()
        }, ContextCompat.getMainExecutor(context))
    }
    override fun preparePlayer(dataSourceUrl: String) {
        val mediaItem = MediaItem.fromUri(dataSourceUrl.toUri())
        player?.setMediaItem(mediaItem)
        _playerState.value = PlayerState.STATE_PREPARED
    }

    override fun startPlayer() {
        try {
            controllerFuture?.get()?.play()
            _playerState.value = PlayerState.STATE_PLAYING
        } catch (e: Exception) {
            Log.e(TAG, "exception in startPlayer() ${e.toString()}")
        }
    }

    override fun pausePlayer() {
        if (controllerFuture?.get()?.isPlaying == true) {
            try {
                controllerFuture?.get()?.pause()
                _playerState.value = PlayerState.STATE_PAUSED
            } catch (e: Exception) {
                Log.e(TAG, "exception in pausePlayer() ${e.toString()}")
            }
        }

    }

    override fun releasePlayer() {
        try {
            controllerFuture?.get()?.release()
            _playerState.value = PlayerState.STATE_DEFAULT
        } catch (e: Exception) {
            Log.e(TAG, "exception in releasePlayer() ${e.toString()}")
        }
    }

    override fun getCurrentPosition(): Int {
        var currentPosition = 0
        try {
            currentPosition = controllerFuture?.get()?.currentPosition?.toInt() ?: 0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "exception in getCurrentPosition() ${e.toString()}")
        }
        return currentPosition
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (mediaSession?.player?.playWhenReady == false || mediaSession?.player?.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            controllerFuture?.get()?.release()
            release()
            mediaSession = null
        }
        controllerFuture = null
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession


    companion object {
        private const val TAG = "PlayerServiceImpl"
    }

}