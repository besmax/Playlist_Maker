package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist) : Long

    suspend fun deletePlaylist(playlist: Playlist)

    fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun saveCover(uri: Uri) : Uri

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Flow<Boolean>


}