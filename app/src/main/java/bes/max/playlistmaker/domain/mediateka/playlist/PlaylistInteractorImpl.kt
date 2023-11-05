package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getAllPlaylists()

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }


    override suspend fun saveCover(uri: Uri): Uri =
        playlistRepository.saveCover(uri)

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) =
        playlistRepository.addTrackToPlaylist(track, playlist)

    override fun getPlaylistById(id: Long): Flow<Playlist> =
        playlistRepository.getPlaylistById(id)

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        playlistRepository.deleteTrackFromPlaylist(trackId = trackId, playlistId = playlistId)
        getPlaylistById(playlistId).collect() {
            val updatedPlaylist = it.copy(tracksNumber = it.tracksNumber - 1)
            playlistRepository.updatePlaylist(updatedPlaylist)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }
}