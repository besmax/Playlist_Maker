package bes.max.playlistmaker.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bes.max.playlistmaker.data.player.Player
import bes.max.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val track: Track) : ViewModel() {

    private val player: Player = Player()
    val playerState = player.playerState
    val playingTime =
        MutableLiveData<String>("00:00") //TODO change this value and update ui with seconds
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { updateTimer() }

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
        updateTimer()
    }

    fun releasePlayer() {
        player.releasePlayer()
        updateTimer()
    }

    fun getCurrentPlayerPositionAsNumber(): Int {
        val timeAsANumber = player.getCurrentPosition()
        return timeAsANumber ?: 0
    }

    fun formatIntToFormattedTimeText(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun updateTimer() {
        val formattedText = formatIntToFormattedTimeText(
            getCurrentPlayerPositionAsNumber()
        )
        when (playerState.value) {
            Player.PlayerState.STATE_PLAYING -> {
                playingTime.value =
                    formattedText
                handler.postDelayed(timerRunnable, TIMER_UPDATE_RATE)
            }

            Player.PlayerState.STATE_PAUSED -> {
                handler.removeCallbacks(timerRunnable)
            }

            else -> {
                handler.removeCallbacks(timerRunnable)
                playingTime.value = DEFAULT_TIMER_TIME
            }
        }
    }

    companion object {
        private const val TIMER_UPDATE_RATE = 500L
        private const val DEFAULT_TIMER_TIME = "00:00"
    }

}
