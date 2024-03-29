package bes.max.playlistmaker.data.dto

data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long = 0L,
    val artworkUrl100: String,
    val collectionName: String = "",
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?
)