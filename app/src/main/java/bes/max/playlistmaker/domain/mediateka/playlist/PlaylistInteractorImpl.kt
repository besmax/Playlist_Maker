package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri
import bes.max.playlistmaker.domain.models.Playlist
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

}