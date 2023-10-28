package bes.max.playlistmaker.domain.mediateka.favorite

interface PlaylistTrackRepository {

    suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long)
}