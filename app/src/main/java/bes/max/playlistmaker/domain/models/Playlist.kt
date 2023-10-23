package bes.max.playlistmaker.domain.models

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val tracks: List<Track>?,
    val tracksNumber: Int = tracks?.size ?: 0
)
