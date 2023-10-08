package bes.max.playlistmaker.domain.mediateka

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrackToFavorite(track: Track)

    suspend fun deleteTrackFromFavorite(track: Track)

    fun getAllFavoriteTracks() : Flow<List<Track>>

    fun getAllIdsOfFavoriteTracks() : Flow<List<Long>>
}