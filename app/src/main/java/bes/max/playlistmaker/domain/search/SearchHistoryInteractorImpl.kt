package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val saveTrackInHistoryUseCase: SaveTrackInHistoryUseCase,
    private val getTracksFromHistoryUseCase: GetTracksFromHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : SearchHistoryInteractor {

    override fun saveTrackToHistory(track: Track) {
        saveTrackInHistoryUseCase.execute(track)
    }

    override fun getTracksFromHistory(): List<Track> =
        getTracksFromHistoryUseCase.execute()
    

    override fun clearHistory() {
        clearHistoryUseCase.execute()
    }
}