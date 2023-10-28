package bes.max.playlistmaker.data.mediateka

import android.net.Uri
import bes.max.playlistmaker.data.db.dao.PlaylistTrackDao
import bes.max.playlistmaker.data.db.dao.PlaylistsDao
import bes.max.playlistmaker.data.db.dao.TrackDao
import bes.max.playlistmaker.data.db.entities.PlaylistTrackEntity
import bes.max.playlistmaker.data.mappers.PlaylistDbMapper
import bes.max.playlistmaker.data.mappers.TrackDbMapper
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
    private val trackDao: TrackDao,
    private val trackDbMapper: TrackDbMapper,
    private val playlistsDao: PlaylistsDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val imageDao: ImageDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistId = playlistsDao.insertPlaylist(PlaylistDbMapper.map(playlist))
        if (!playlist.tracks.isNullOrEmpty()) {
            playlist.tracks.forEach { track ->
                trackDao.insertTrack(trackDbMapper.map(track))
                playlistTrackDao.insert(PlaylistTrackEntity(track.trackId, playlistId))
            }
        }
    }


    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsDao.deletePlaylist(PlaylistDbMapper.map(playlist))
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlistEntities = playlistsDao.getAllPlaylists()

        val playlistsWithoutTracks = playlistEntities.map { playlistEntity ->
            PlaylistDbMapper.map(playlistEntity)
        }
        val playlists = playlistsWithoutTracks.map { playlist ->
            playlist.copy(
                tracks = playlistTrackDao.getAllTracksFromPlaylist(playlist.id).map {
                    trackDbMapper.map(it)
                }
            )
        }
        emit(playlists)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveCover(uri: Uri): Uri {
        return imageDao.saveImage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean> =
        flow {
            var result = false
            if (!playlist.tracks.isNullOrEmpty()) {
                trackDao.insertTrack(trackDbMapper.map(track))
                playlistsDao.updatePlaylist(PlaylistDbMapper.map(playlist))
                playlistTrackDao.insert(PlaylistTrackEntity(track.trackId, playlist.id))
                result = true
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}