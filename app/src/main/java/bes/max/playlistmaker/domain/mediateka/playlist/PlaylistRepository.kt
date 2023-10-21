package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun saveCover(uri: Uri) : Uri


}