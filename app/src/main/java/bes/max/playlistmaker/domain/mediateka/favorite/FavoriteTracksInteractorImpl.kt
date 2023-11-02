package bes.max.playlistmaker.domain.mediateka.favorite

import android.util.Log
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        val favTrack = track.copy(isFavorite = true)
        favoriteTracksRepository.addTrack(favTrack)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        val favTrack = track.copy(isFavorite = false)
        favoriteTracksRepository.addTrack(favTrack)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> =
        favoriteTracksRepository.getAll()

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> =
        favoriteTracksRepository.getAllIdsOfFavoriteTracks()

    override fun trackIsFavorite(trackId: Long): Flow<Boolean> = flow {
        var result = false
        try {
            val track = favoriteTracksRepository.getTrackById(trackId)
            result = track.isFavorite
        } catch (e: Exception) {
            Log.d(TAG, "Track is not found in db")
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val TAG = "FavoriteTracksInteractorImpl"
    }
}