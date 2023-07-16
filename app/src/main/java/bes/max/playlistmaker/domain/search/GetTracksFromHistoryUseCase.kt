package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.api.TracksRepository

class GetTracksFromHistoryUseCase(private val tracksRepository: TracksRepository) {

    fun execute() = tracksRepository.getTracksFromHistory()

}