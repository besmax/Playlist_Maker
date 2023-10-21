package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    var coverUri: Uri? = null

    fun createPlaylist(name: String, description: String? = null) {
        viewModelScope.launch {
            coverUri?.let { saveImageToPrivateStorage(it) }
            val playlist = Playlist(
                name = name,
                description = description,
                coverPath = coverUri?.toString(),
                tracks = null
            )
            playlistInteractor.createPlaylist(playlist)
        }
    }

    private suspend fun saveImageToPrivateStorage(uri: Uri) {
        coverUri = playlistInteractor.saveCover(uri)

    }

}