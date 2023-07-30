package bes.max.playlistmaker.data.network

import bes.max.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApiService {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String) : Response<TrackSearchResponse>

}
