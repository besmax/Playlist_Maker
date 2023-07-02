package bes.max.playlistmaker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import bes.max.playlistmaker.domain.Player
import bes.max.playlistmaker.model.Track

class PlayerViewModel(val track: Track) : ViewModel() {

    private val player: Player = Player()
    val playerState = player.playerState

    init {
        player.createNewMediaPlayer()
        player.preparePlayer(track.previewUrl)
    }

    fun playbackControl() {
        Log.wtf("PlayerViewModel", "playerState.value ${player.playerState.value.toString()}")
        when (player.playerState.value) {
            Player.PlayerState.STATE_PREPARED, Player.PlayerState.STATE_PAUSED -> {
                player.startPlayer()
            }

            Player.PlayerState.STATE_PLAYING -> {
                player.pausePlayer()
            }

            else -> {}
        }
    }

    fun releasePlayer() {
        player.releasePlayer()
    }


}
