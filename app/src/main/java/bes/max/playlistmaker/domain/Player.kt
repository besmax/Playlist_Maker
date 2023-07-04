package bes.max.playlistmaker.domain

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Player {

    private var mediaPlayer: MediaPlayer? = null
    private val _playerState = MutableLiveData<PlayerState>(PlayerState.STATE_DEFAULT)
    val playerState: LiveData<PlayerState> = _playerState

    fun createNewMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    fun preparePlayer(dataSourceUrl: String) {
        if (mediaPlayer != null) {
            with(mediaPlayer!!) {
                setDataSource(dataSourceUrl)
                prepareAsync()
                setOnPreparedListener { _playerState.value = PlayerState.STATE_PREPARED }
                setOnCompletionListener { _playerState.value = PlayerState.STATE_PREPARED }
            }
        }
    }

    fun startPlayer() {
        if (_playerState.value == PlayerState.STATE_PREPARED || _playerState.value == PlayerState.STATE_PAUSED) {
            mediaPlayer?.start()
            _playerState.value = PlayerState.STATE_PLAYING
        }
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
        _playerState.value = PlayerState.STATE_PAUSED
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    fun getCurrentPosition(): Int? =
        mediaPlayer?.currentPosition

    sealed interface PlayerState {
        object STATE_DEFAULT : PlayerState
        object STATE_PREPARED : PlayerState
        object STATE_PLAYING : PlayerState
        object STATE_PAUSED : PlayerState
    }
}