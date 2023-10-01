package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getTracksFromHistory() : Flow<List<Track>>

    fun clearHistory()
}