package bes.max.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val track: Track, private val playerInteractor: PlayerInteractor) : ViewModel() {

    val playerState = playerInteractor.state.asLiveData()
    val playingTime =
        MutableLiveData<String>("00:00")
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { updateTimer() }

    init {
        playerInteractor.preparePlayer(track.previewUrl ?: "")
    }

    fun playbackControl() {
        when (playerState.value) {

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                playerInteractor.play()
            }

            PlayerState.STATE_PLAYING -> {
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

    private fun getCurrentPlayerPositionAsNumber() = playerInteractor.getCurrentTime()

    private fun formatIntToFormattedTimeText(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun updateTimer() {
        val formattedText = formatIntToFormattedTimeText(
            getCurrentPlayerPositionAsNumber()
        )
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                playingTime.value =
                    formattedText
                handler.postDelayed(timerRunnable, TIMER_UPDATE_RATE)
            }

            PlayerState.STATE_PAUSED -> {
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