package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val trackRepository: TracksRepository
) : SearchHistoryInteractor {

    override fun saveTrackToHistory(track: Track) {
        trackRepository.saveTrackInHistory(track)
    }

    override fun getTracksFromHistory(): List<Track> =
        trackRepository.getTracksFromHistory()


    override fun clearHistory() {
        trackRepository.clearHistory()
    }
}