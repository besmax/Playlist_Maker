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
    private val _isPlaylistAdded: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    val isPlaylistAdded: LiveData<Pair<Boolean, String>> = _isPlaylistAdded

    init {
        preparePlayer()
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

    private fun preparePlayer() {
        playerInteractor.preparePlayer(track.previewUrl ?: "")
        checkIsFavorite()
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
            val trackForAdding = track.copy(isFavorite = true)
            favoriteTracksInteractor.addTrackToFavorite(trackForAdding)
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
        viewModelScope.launch {
            favoriteTracksInteractor.trackIsFavorite(trackId = track.trackId).collect() { isFav ->
                _isFavorite.postValue(isFav)
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

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addTrackToPlaylist(track, playlist).collect() {
                _isPlaylistAdded.postValue(Pair(it, playlist.name))
            }
        }
    }

    companion object {
        private const val TIMER_UPDATE_RATE = 300L
        private const val DEFAULT_TIMER_TIME = "00:00"
    }

}
