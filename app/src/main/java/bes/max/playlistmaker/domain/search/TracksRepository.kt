package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Track

interface TracksRepository {

    suspend fun searchTracks(searchRequest: String) : List<Track>

    fun saveTrackInHistory(track: Track)

    fun getTracksFromHistory() : List<Track>

    fun clearHistory()
}