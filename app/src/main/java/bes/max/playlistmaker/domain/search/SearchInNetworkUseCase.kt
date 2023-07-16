package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.models.Track

class SearchInNetworkUseCase(private val tracksRepository: TracksRepository) {

    suspend fun execute(searchRequest: String): List<Track> {
        return tracksRepository.searchTracks(searchRequest)
    }

}