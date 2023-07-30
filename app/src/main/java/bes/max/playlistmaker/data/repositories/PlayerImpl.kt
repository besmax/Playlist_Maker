package bes.max.playlistmaker.data.repositories

import android.media.AudioAttributes
import android.media.MediaPlayer
import bes.max.playlistmaker.domain.api.Player
import bes.max.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerImpl : Player {

    private var mediaPlayer: MediaPlayer? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.STATE_DEFAULT)
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        createNewMediaPlayer()
    }

    private fun createNewMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    override fun preparePlayer(dataSourceUrl: String) {
        if (mediaPlayer != null) {
            with(mediaPlayer!!) {
                setDataSource(dataSourceUrl)
                prepareAsync()
                setOnPreparedListener { _playerState.value = PlayerState.STATE_PREPARED }
                setOnCompletionListener { _playerState.value = PlayerState.STATE_PREPARED }
            }
        }
    }

    override fun startPlayer() {
        if (_playerState.value == PlayerState.STATE_PREPARED || _playerState.value == PlayerState.STATE_PAUSED) {
            mediaPlayer?.start()
            _playerState.value = PlayerState.STATE_PLAYING
        }
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        _playerState.value = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int =
        mediaPlayer?.currentPosition ?: 0

}