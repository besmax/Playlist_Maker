package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    var coverUri: Uri? = null

    fun createPlaylist(name: String, description: String? = null, trackArg: String? = null) {
        val track = trackArg.let { Gson().fromJson(it, Track::class.java) }
        viewModelScope.launch {
            var coverUriInStorage: Uri? = null
            if (coverUri != null) {
                coverUriInStorage = saveImageToPrivateStorage(coverUri!!)
                Log.d(TAG, coverUriInStorage.toString())
            }
            Log.d(TAG, "coverUri = ${coverUri.toString()}")
            val playlist = Playlist(
                name = name,
                description = description,
                coverPath = if (coverUriInStorage != null) coverUriInStorage.toString() else null,
                tracks = if (track != null) listOf(track) else null,
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