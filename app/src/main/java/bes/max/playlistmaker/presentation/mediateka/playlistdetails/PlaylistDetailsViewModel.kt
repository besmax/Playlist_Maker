package bes.max.playlistmaker.presentation.mediateka.playlistdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.mediateka.playlistdetails.SharePlaylistUseCase
import bes.max.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val playlistInteractor: PlaylistInteractor,
    private val sharePlaylistUseCase: SharePlaylistUseCase
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistDetailsScreenState> =
        MutableLiveData(PlaylistDetailsScreenState.Default)
    val screenState: LiveData<PlaylistDetailsScreenState> = _screenState
    private val playlistId = savedStateHandle.get<Long>("playlistId")!!

    init {
        if (playlistId != null) {
            getPlaylist(playlistId)
        }
    }

    private fun getPlaylist(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect() { playlist ->
                var durationSum = 0L
                playlist.tracks?.forEach { durationSum += it.trackTimeMillis }
                val playlistDetails = PlaylistDetails(
                    title = playlist.name,
                    description = playlist.description ?: "",
                    tracks = playlist.tracks ?: emptyList(),
                    durationSum = durationSum,
                    cover = playlist.coverPath,
                    playlist = playlist
                )
                _screenState.postValue(PlaylistDetailsScreenState.Content(playlistDetails))
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        _screenState.value = PlaylistDetailsScreenState.Editing
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(trackId = trackId, playlistId = playlistId)
            getPlaylist(playlistId)
        }
    }

    fun sharePlaylist(playlist: Playlist) {
        try {
            sharePlaylistUseCase.execute(playlist)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private const val TAG = "PlaylistDetailsViewModel"
    }

}