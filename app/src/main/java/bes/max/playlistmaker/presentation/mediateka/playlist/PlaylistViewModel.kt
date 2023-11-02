package bes.max.playlistmaker.presentation.mediateka.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Empty)
    val screenState: LiveData<PlaylistScreenState> = _screenState

    init {
        getPlaylists()
    }

    fun getPlaylists() {
        _screenState.value = PlaylistScreenState.Loading
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().flowOn(Dispatchers.Main).collect { playlists ->
                if (playlists.isNullOrEmpty()) {
                    _screenState.postValue(PlaylistScreenState.Empty)
                } else {
                    _screenState.postValue(PlaylistScreenState.Content(playlists))
                }
            }

        }
    }

}