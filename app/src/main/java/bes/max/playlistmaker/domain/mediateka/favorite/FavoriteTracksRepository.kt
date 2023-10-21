package bes.max.playlistmaker.domain.mediateka.favorite

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {


    suspend fun addTrackToFavorite(track: Track)

    suspend fun deleteTrackFromFavorite(track: Track)

    fun getAllFavoriteTracks() : Flow<List<Track>>

    fun getAllIdsOfFavoriteTracks() : Flow<List<Long>>

}