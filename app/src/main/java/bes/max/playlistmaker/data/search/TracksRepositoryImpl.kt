package bes.max.playlistmaker.data.search

import bes.max.playlistmaker.data.dto.TrackSearchRequest
import bes.max.playlistmaker.data.dto.TrackSearchResponse
import bes.max.playlistmaker.data.mappers.TrackDtoMapper
import bes.max.playlistmaker.data.network.NetworkClient
import bes.max.playlistmaker.domain.models.Resource
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.search.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryDao: SearchHistoryDao,
    private val trackDtoMapper: TrackDtoMapper
) : TracksRepository {

    override suspend fun searchTracks(searchRequest: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.searchTracks(TrackSearchRequest(searchRequest))
        when (response.resultCode) {
            200 -> {
                val data = (response as TrackSearchResponse).results.map {
                    trackDtoMapper.trackDtoToTrack(it)
                }
                emit(Resource.Success(data = data))
            }

            -1 -> emit(Resource.Error(message = "No Internet"))
            else -> emit(Resource.Error(message = "Server error"))
        }
    }

    override fun saveTrackInHistory(track: Track) {
        searchHistoryDao.saveTrack(track)
    }

    override fun getTracksFromHistory(): Flow<List<Track>> {
        return searchHistoryDao.getHistoryTracks()
    }

    override fun clearHistory() {
        searchHistoryDao.clearTracksHistory()
    }


}