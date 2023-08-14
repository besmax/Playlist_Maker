package bes.max.playlistmaker.data.network

import bes.max.playlistmaker.data.dto.Response
import bes.max.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val iTunesSearchApiService: ITunesSearchApiService) :
    NetworkClient {

    override suspend fun searchTracks(dto: Any): Response {
        return when (dto) {
            is TrackSearchRequest -> {
                val response = iTunesSearchApiService.search(dto.searchRequest)
                val responseBody = response.body() ?: Response()
                responseBody.apply { resultCode = response.code() }
            }

            else -> Response().apply { resultCode = 400 }
        }
    }
}