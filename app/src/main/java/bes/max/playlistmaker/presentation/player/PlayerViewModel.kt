package bes.max.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.favorite.FavoriteTracksInteractor
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) :
    ViewModel() {

    val playerState = playerInteractor.state.asLiveData()
    private val _playingTime = MutableLiveData<String>("00:00")
    val playingTime: LiveData<String> = _playingTime
    private var timerJob: Job? = null
    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite
    private val _playlists: MutableLiveData<List<Playlist>> = MutableLiveData()
    val playlists: LiveData<List<Playlist>> = _playlists

    init {
        playerInteractor.preparePlayer(track.previewUrl ?: "")
        checkIsFavorite()
        getPlaylists()
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

    fun pausePlayer() {
        playerInteractor.pause()
    }

    fun releasePlayer() {
        playerInteractor.release()
        updateTimer()
    }

    private fun formatIntToFormattedTimeText(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun updateTimer() {
        val formattedText = formatIntToFormattedTimeText(
            playerInteractor.getCurrentTime()
        )
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                _playingTime.value = formattedText
                timerJob = viewModelScope.launch {
                    delay(TIMER_UPDATE_RATE)
                    updateTimer()
                }
            }

            PlayerState.STATE_PAUSED -> {
                timerJob?.cancel()
            }

            else -> {
                timerJob?.cancel()
                _playingTime.value = DEFAULT_TIMER_TIME
            }
        }
    }

    fun addToFavorite(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.addTrackToFavorite(track)
            _isFavorite.postValue(true)
        }
    }

    fun deleteFromFavorite(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.deleteTrackFromFavorite(track)
            _isFavorite.postValue(false)
        }
    }

    private fun checkIsFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.getAllIdsOfFavoriteTracks().collect() {
                _isFavorite.postValue(it.contains(track.trackId))
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                _playlists.postValue(it)
            }
        }
    }

    companion object {
        private const val TIMER_UPDATE_RATE = 300L
        private const val DEFAULT_TIMER_TIME = "00:00"
    }

}
