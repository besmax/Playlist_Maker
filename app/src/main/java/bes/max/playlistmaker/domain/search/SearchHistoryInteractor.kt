package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getTracksFromHistory() : List<Track>

    fun clearHistory()
}