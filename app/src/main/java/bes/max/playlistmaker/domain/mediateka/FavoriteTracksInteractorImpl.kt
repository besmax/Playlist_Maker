package bes.max.playlistmaker.domain.mediateka

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksRepository.addTrackToFavorite(track)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTracksRepository.deleteTrackFromFavorite(track)
    }

    override suspend fun getAllFavoriteTracks(): Flow<List<Track>> =
        favoriteTracksRepository.getAllFavoriteTracks()

    override suspend fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> =
        favoriteTracksRepository.getAllIdsOfFavoriteTracks()
}