package bes.max.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.favorite.FavoriteTracksInteractor
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    val track: Track,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var playerService: PlayerService? = null
    val playerState by lazy {
        playerService?.playerState?.asLiveData()
    }
    val playingTime: LiveData<Int>? by lazy {
        playerService?.currentPosition?.asLiveData()
    }
    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite
    private val _playlists: MutableLiveData<List<Playlist>> = MutableLiveData()
    val playlists: LiveData<List<Playlist>> = _playlists
    private val _isPlaylistAdded: MutableLiveData<Pair<Boolean?, String>> = MutableLiveData()
    val isPlaylistAdded: LiveData<Pair<Boolean?, String>> = _isPlaylistAdded
    private val dateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }


    init {
        checkIsFavorite()
        getPlaylists()
    }

    fun playbackControl() {
        playerService?.playback()
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

    fun clearIsPlaylistAdded() {
        _isPlaylistAdded.value = Pair(null, "")
    }

    fun setPlayerService(service: PlayerService) {
        playerService = service
    }

    fun formatIntToFormattedTimeText(time: Int): String {
        return dateFormatter.format(time)
    }

    fun showNotification() {
        playerService?.showNotification()
    }

    fun hideNotification() {
        playerService?.hideNotification()
    }

}
