package bes.max.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "PlayerImpl"

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.STATE_DEFAULT)
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    private val _currentPosition = MutableSharedFlow<Int>(0)
    override val currentPosition: SharedFlow<Int> = _currentPosition.asSharedFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var timerJob: Job? = null

    override fun preparePlayer(dataSourceUrl: String) {
        with(mediaPlayer) {
            try {
                setDataSource(dataSourceUrl)
                prepareAsync()
                setOnPreparedListener { _playerState.value = PlayerState.STATE_PREPARED }
                setOnCompletionListener { _playerState.value = PlayerState.STATE_PREPARED }
            } catch (e: Exception) {
                setDataSource(dataSourceUrl)
                prepareAsync()
                setOnPreparedListener { _playerState.value = PlayerState.STATE_PREPARED }
                setOnCompletionListener { _playerState.value = PlayerState.STATE_PREPARED }
            }
        }

    }

    override fun startPlayer() {
        try {
            if (_playerState.value == PlayerState.STATE_PREPARED || _playerState.value == PlayerState.STATE_PAUSED) {
                mediaPlayer.start()
                _playerState.value = PlayerState.STATE_PLAYING
                timerJob = coroutineScope.launch {
                    while (_playerState.value == PlayerState.STATE_PLAYING) {
                        _currentPosition.emit(mediaPlayer.currentPosition)
                        delay(TIMER_UPDATE_RATE)
                    }
                    if (_playerState.value == PlayerState.STATE_PREPARED) {
                        _currentPosition.emit(DEFAULT_TIMER_TIME)
                    }
                    timerJob?.cancel()
                }
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "exception in startPlayer() ${e.toString()}")
        }

    }

    override fun pausePlayer() {
        try {
            mediaPlayer.pause()
            _playerState.value = PlayerState.STATE_PAUSED
            timerJob?.cancel()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "exception in pausePlayer() ${e.toString()}")
        }

    }

    override fun releasePlayer() {
        try {
            mediaPlayer.reset()
            _playerState.value = PlayerState.STATE_DEFAULT
        } catch (e: IllegalStateException) {
            Log.e(TAG, "exception in releasePlayer() ${e.toString()}")
        }
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

    companion object {
        private const val TIMER_UPDATE_RATE = 300L
        private const val DEFAULT_TIMER_TIME = 0
    }

}