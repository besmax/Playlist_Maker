package bes.max.playlistmaker.data.mediateka

import bes.max.playlistmaker.data.db.dao.PlaylistTrackDao
import bes.max.playlistmaker.data.db.entities.PlaylistTrackEntity
import bes.max.playlistmaker.domain.mediateka.favorite.PlaylistTrackRepository

class PlaylistTrackRepositoryImpl(
    private val playlistTrackDao: PlaylistTrackDao,
) : PlaylistTrackRepository {
    override suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        playlistTrackDao.insert(
            PlaylistTrackEntity(trackId, playlistId)
        )
    }


}