package bes.max.playlistmaker.data.mediateka

import bes.max.playlistmaker.data.db.FavoriteTracksDao
import bes.max.playlistmaker.data.mappers.TrackDbMapper
import bes.max.playlistmaker.domain.mediateka.FavoriteTracksRepository
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val dao: FavoriteTracksDao,
    private val convertor: TrackDbMapper
) : FavoriteTracksRepository {
    override suspend fun addTrackToFavorite(track: Track) {
        dao.insertTrack(convertor.map(track))
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        dao.deleteTrack(convertor.map(track))
    }

    override suspend fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = dao.getAllFavoriteTracks().sortedBy { it.addingDate }.map { convertor.map(it) }
        emit(tracks)
    }

    override suspend fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> = flow {
        emit(dao.getAllIdsOfFavoriteTracks())
    }
}