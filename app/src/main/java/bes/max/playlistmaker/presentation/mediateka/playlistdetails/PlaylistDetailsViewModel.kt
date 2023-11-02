package bes.max.playlistmaker.presentation.mediateka.playlistdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistDetailsScreenState> =
        MutableLiveData(PlaylistDetailsScreenState.Default)
    val screenState: LiveData<PlaylistDetailsScreenState> = _screenState

    init {
        val playlistId = savedStateHandle.get<Long>("playlistId")
        if (playlistId != null) {
            getPlaylist(playlistId)
        }
    }

    private fun getPlaylist(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect() { playlist ->
                _screenState.postValue(PlaylistDetailsScreenState.Content(playlist))
            }
        }

    }

}