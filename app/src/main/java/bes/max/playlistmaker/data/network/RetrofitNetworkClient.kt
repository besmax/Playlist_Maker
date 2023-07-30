package bes.max.playlistmaker.data.network

import bes.max.playlistmaker.data.NetworkClient
import bes.max.playlistmaker.data.dto.Response
import bes.max.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchApiService by lazy {
        retrofit.create(ITunesSearchApiService::class.java)
    }

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