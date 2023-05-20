package bes.max.playlistmaker.network

import bes.max.playlistmaker.model.ITunesSearchApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://itunes.apple.com"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ITunesSearchApiService {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<ITunesSearchApiResponse>

}

object ITunesSearchApi {
    val iTunesSearchApiService by lazy {
        retrofit.create(ITunesSearchApiService::class.java)
    }
}