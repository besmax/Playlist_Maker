package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun createPlaylist(name: String, description: String? = null, trackArg: String? = null, uri: Uri?) {
        val track = trackArg.let { Gson().fromJson(it, Track::class.java) }
        viewModelScope.launch(Dispatchers.IO) {
            val savedCoverUri = uri?.let { saveImageToPrivateStorage(it) }
            val playlist = Playlist(
                name = name,
                description = description,
                coverPath = if (savedCoverUri != null) savedCoverUri.toString() else null,
                tracks = if (track != null) listOf(track) else null,
                tracksNumber = if (track != null) 1 else 0
            )
            playlistInteractor.createPlaylist(playlist)
        }
    }

    private suspend fun saveImageToPrivateStorage(uri: Uri) =
            playlistInteractor.saveCover(uri)

    companion object {
        private const val TAG = "NewPlaylistViewModel"
    }


}