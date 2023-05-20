package bes.max.playlistmaker.model

data class ITunesSearchApiResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)
