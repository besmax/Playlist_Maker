package bes.max.playlistmaker.ui

import androidx.lifecycle.ViewModel
import bes.max.playlistmaker.domain.Player
import bes.max.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val track: Track) : ViewModel() {

    private val player: Player = Player()
    val playerState = player.playerState

    init {
        player.createNewMediaPlayer()
        player.preparePlayer(track.previewUrl)
    }

    fun playbackControl() {
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

    fun getCurrentPositionAsFormattedText(): String {
        val timeAsANumber = player.getCurrentPosition()
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeAsANumber)
    }


}
