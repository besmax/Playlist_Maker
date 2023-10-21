package bes.max.playlistmaker.domain.mediateka.favorite

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

    override fun getAllFavoriteTracks(): Flow<List<Track>> =
        favoriteTracksRepository.getAllFavoriteTracks()

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> =
        favoriteTracksRepository.getAllIdsOfFavoriteTracks()
}