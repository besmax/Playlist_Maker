package bes.max.playlistmaker.data.network

import android.content.Context
import bes.max.playlistmaker.data.dto.Response
import bes.max.playlistmaker.data.dto.TrackSearchRequest
import bes.max.playlistmaker.presentation.utils.ConnectionChecker.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesSearchApiService: ITunesSearchApiService,
    private val context: Context
) :
    NetworkClient {

    override suspend fun searchTracks(dto: Any): Response {
        if (!isConnected(context)) {
            return Response().apply { resultCode = -1 }
        }

        return when (dto) {
            is TrackSearchRequest -> {
                withContext(Dispatchers.IO) {
                    try {
                        val response = iTunesSearchApiService.search(dto.searchRequest)
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }
            }

            else -> Response().apply { resultCode = 400 }
        }
    }
}