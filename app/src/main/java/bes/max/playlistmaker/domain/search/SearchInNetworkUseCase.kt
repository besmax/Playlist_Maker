package bes.max.playlistmaker.domain.search

import bes.max.playlistmaker.domain.models.Resource
import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchInNetworkUseCase(private val tracksRepository: TracksRepository) {

    suspend fun execute(searchRequest: String): Flow<Resource<List<Track>>> {
        return tracksRepository.searchTracks(searchRequest)
    }

}