package bes.max.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import bes.max.playlistmaker.data.dto.Response
import bes.max.playlistmaker.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesSearchApiService: ITunesSearchApiService,
    private val context: Context
) :
    NetworkClient {

    override suspend fun searchTracks(dto: Any): Response {
        if (!isConnected()) {
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

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}