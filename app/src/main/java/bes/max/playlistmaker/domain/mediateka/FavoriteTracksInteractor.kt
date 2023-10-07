package bes.max.playlistmaker.domain.mediateka

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrackToFavorite(track: Track)

    suspend fun deleteTrackFromFavorite(track: Track)

    suspend fun getAllFavoriteTracks() : Flow<List<Track>>

    suspend fun getAllIdsOfFavoriteTracks() : Flow<List<Long>>
}