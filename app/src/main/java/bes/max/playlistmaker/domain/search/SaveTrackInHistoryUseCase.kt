package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.models.Track

class SaveTrackInHistoryUseCase(private val tracksRepository: TracksRepository) {

    fun execute(track: Track) {
        tracksRepository.saveTrackInHistory(track)
    }
}