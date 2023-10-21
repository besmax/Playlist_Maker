package bes.max.playlistmaker.data.mediateka

import android.net.Uri
import bes.max.playlistmaker.data.db.dao.PlaylistsDao
import bes.max.playlistmaker.data.mappers.PlaylistDbMapper
import bes.max.playlistmaker.domain.mediateka.playlist.ImageDao
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistRepository
import bes.max.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistsDao: PlaylistsDao,
    private val imageDao: ImageDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsDao.insertPlaylist(PlaylistDbMapper.map(playlist))
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsDao.deletePlaylist(PlaylistDbMapper.map(playlist))
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsDao.updatePlaylist(PlaylistDbMapper.map(playlist))
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlistEntities = playlistsDao.getAllPlaylists()
        emit(playlistEntities.map { PlaylistDbMapper.map(it) })
    }.flowOn(Dispatchers.IO)

    override suspend fun saveCover(uri: Uri): Uri = imageDao.saveImage(uri)

}