package bes.max.playlistmaker.data

import bes.max.playlistmaker.data.dto.Response

interface NetworkClient {

    suspend fun searchTracks(dto: Any) : Response

}