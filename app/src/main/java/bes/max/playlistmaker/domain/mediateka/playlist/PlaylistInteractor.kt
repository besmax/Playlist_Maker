package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun saveCover(uri: Uri): Uri

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean>

    fun getPlaylistById(id: Long): Flow<Playlist>

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    suspend fun deletePlaylist(playlist: Playlist)
}