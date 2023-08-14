package bes.max.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.player.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "PlayerImpl"

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.STATE_DEFAULT)
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    override fun preparePlayer(dataSourceUrl: String) {
        with(mediaPlayer) {
            setDataSource(dataSourceUrl)
            prepareAsync()
            setOnPreparedListener { _playerState.value = PlayerState.STATE_PREPARED }
            setOnCompletionListener { _playerState.value = PlayerState.STATE_PREPARED }
        }

    }

    override fun startPlayer() {
        if (_playerState.value == PlayerState.STATE_PREPARED || _playerState.value == PlayerState.STATE_PAUSED) {
            mediaPlayer.start()
            _playerState.value = PlayerState.STATE_PLAYING
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        _playerState.value = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        var currentPosition = 0
        try {
            currentPosition = mediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            Log.e(TAG, "exception in getCurrentPosition() ${e.toString()}")
        }
        return currentPosition
    }

}