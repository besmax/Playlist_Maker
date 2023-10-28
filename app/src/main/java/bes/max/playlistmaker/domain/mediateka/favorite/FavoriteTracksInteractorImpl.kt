package bes.max.playlistmaker.domain.mediateka.favorite

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksRepository.addTrack(track)

    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> =
        favoriteTracksRepository.getAll()

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> =
        favoriteTracksRepository.getAllIdsOfFavoriteTracks()
}