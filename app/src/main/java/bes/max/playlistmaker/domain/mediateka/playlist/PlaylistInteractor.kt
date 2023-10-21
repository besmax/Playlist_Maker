package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun saveCover(uri: Uri): Uri
}