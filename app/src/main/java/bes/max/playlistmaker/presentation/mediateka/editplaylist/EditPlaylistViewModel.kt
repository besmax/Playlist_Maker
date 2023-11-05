package bes.max.playlistmaker.presentation.mediateka.editplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.presentation.mediateka.newplaylist.NewPlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    savedStateHandle: SavedStateHandle,
    private val playlistInteractor: PlaylistInteractor
) : NewPlaylistViewModel(playlistInteractor) {

    private val playlistId = savedStateHandle.get<Long>("playlistId")!!

    private val _playlist = MutableLiveData<EditPlaylistScreenState>()
    val playlist: LiveData<EditPlaylistScreenState> = _playlist

    init {
        getPlaylist(playlistId)
    }

    private fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect() { playlist ->
                _playlist.postValue(EditPlaylistScreenState.Editing(playlist))
            }
        }
    }

    fun updatePlaylist(name: String, description: String? = null, uri: Uri? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedPlaylist =
                (_playlist.value as EditPlaylistScreenState.Editing).playlist.copy(
                    name = name,
                    description = if (!description.isNullOrBlank()) description else (_playlist.value as EditPlaylistScreenState.Editing).playlist.description,
                    coverPath = if (uri != null) saveImageToPrivateStorage(uri).toString() else (_playlist.value as EditPlaylistScreenState.Editing).playlist.coverPath
                )
            playlistInteractor.updatePlaylist(updatedPlaylist)
            _playlist.postValue(EditPlaylistScreenState.Updated)
        }
    }

    private suspend fun saveImageToPrivateStorage(uri: Uri) =
        playlistInteractor.saveCover(uri)

    companion object {
        private const val TAG = "EditPlaylistViewModel"
    }

}