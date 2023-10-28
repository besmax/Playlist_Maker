package bes.max.playlistmaker.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val tracks: List<Track>?,
    val tracksNumber: Int = 0
)
