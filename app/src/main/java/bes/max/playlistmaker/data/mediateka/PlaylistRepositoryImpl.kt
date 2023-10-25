package bes.max.playlistmaker.data.mediateka

import android.net.Uri
import bes.max.playlistmaker.data.db.dao.PlaylistsDao
import bes.max.playlistmaker.data.mappers.PlaylistDbMapper
import bes.max.playlistmaker.domain.mediateka.playlist.ImageDao
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistRepository
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistsDao: PlaylistsDao,
    private val imageDao: ImageDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist): Long = withContext(Dispatchers.IO) {
        playlistsDao.insertPlaylist(PlaylistDbMapper.map(playlist))
    }


    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsDao.deletePlaylist(PlaylistDbMapper.map(playlist))
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlistEntities = playlistsDao.getAllPlaylists()
        emit(playlistEntities.map { playlistEntity -> PlaylistDbMapper.map(playlistEntity) })
    }.flowOn(Dispatchers.IO)

    override suspend fun saveCover(uri: Uri): Uri {
        return imageDao.saveImage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean> =
        flow {
            var updatedTracks = mutableListOf<Track>()
            if (playlist.tracks != null) {
                updatedTracks = playlist.tracks.toMutableList()
            }
            var result = !updatedTracks.contains(track)
            if (result) {
                updatedTracks.add(track)
                val updatedPlaylist = playlist.copy(
                    tracks = updatedTracks.toList(),
                    tracksNumber = playlist.tracksNumber + 1
                )
                playlistsDao.updatePlaylist(PlaylistDbMapper.map(updatedPlaylist))
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}