package bes.max.playlistmaker.data.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bes.max.playlistmaker.domain.api.Player

class PlayerImpl : Player {

    private var mediaPlayer: MediaPlayer? = null
    private val _playerState = MutableLiveData<Player.PlayerState>(Player.PlayerState.STATE_DEFAULT)
    override val playerState: LiveData<Player.PlayerState> = _playerState

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
                setOnPreparedListener { _playerState.value = Player.PlayerState.STATE_PREPARED }
                setOnCompletionListener { _playerState.value = Player.PlayerState.STATE_PREPARED }
            }
        }
    }

    override fun startPlayer() {
        if (_playerState.value == Player.PlayerState.STATE_PREPARED || _playerState.value == Player.PlayerState.STATE_PAUSED) {
            mediaPlayer?.start()
            _playerState.value = Player.PlayerState.STATE_PLAYING
        }
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        _playerState.value = Player.PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        _playerState.value = Player.PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int =
        mediaPlayer?.currentPosition ?: 0

}