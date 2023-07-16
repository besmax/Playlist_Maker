package bes.max.playlistmaker.data.repositories

import bes.max.playlistmaker.data.NetworkClient
import bes.max.playlistmaker.data.dao.SearchHistoryDao
import bes.max.playlistmaker.data.dto.TrackSearchRequest
import bes.max.playlistmaker.data.dto.TrackSearchResponse
import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryDao: SearchHistoryDao
) : TracksRepository {

    override suspend fun searchTracks(searchRequest: String): List<Track> {
        val response = networkClient.searchTracks(TrackSearchRequest(searchRequest))
        when (response.resultCode) {
            //TODO add mapper?
            200 -> return (response as TrackSearchResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
            else -> return emptyList()
        }
    }

    override fun saveTrackInHistory(track: Track) {
        searchHistoryDao.saveTrack(track)
    }

    override fun getTracksFromHistory(): List<Track> {
        return searchHistoryDao.getHistoryTracks()
    }

    override fun clearHistory() {
        searchHistoryDao.clearTracksHistory()
    }


}