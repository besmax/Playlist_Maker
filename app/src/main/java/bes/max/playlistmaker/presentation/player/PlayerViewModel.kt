package bes.max.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bes.max.playlistmaker.domain.api.Player
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val track: Track, private val playerInteractor: PlayerInteractor) : ViewModel() {

    val playerState = playerInteractor.state
    val playingTime =
        MutableLiveData<String>("00:00")
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { updateTimer() }

    init {
        playerInteractor.preparePlayer(track.previewUrl ?: "")
    }

    fun playbackControl() {
        when (playerState.value) {

            Player.PlayerState.STATE_PREPARED, Player.PlayerState.STATE_PAUSED -> {
                playerInteractor.play()
            }

            Player.PlayerState.STATE_PLAYING -> {
                playerInteractor.pause()
            }

            else -> {}
        }
        updateTimer()
    }

    fun releasePlayer() {
        playerInteractor.release()
        updateTimer()
    }

    fun getCurrentPlayerPositionAsNumber() = playerInteractor.getCurrentTime()

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
