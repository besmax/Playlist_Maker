package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.api.TracksRepository

class ClearHistoryUseCase(private val tracksRepository: TracksRepository) {

    fun execute() {
        tracksRepository.clearHistory()
    }
}