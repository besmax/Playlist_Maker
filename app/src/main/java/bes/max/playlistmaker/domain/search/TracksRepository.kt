package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Resource
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun searchTracks(searchRequest: String) : Flow<Resource<List<Track>>>

    fun saveTrackInHistory(track: Track)

    fun getTracksFromHistory() : Flow<List<Track>>

    fun clearHistory()
}