package bes.max.playlistmaker.data.dao

import bes.max.playlistmaker.domain.models.Track

interface SearchHistoryDao {

    fun saveTrack(track: Track)

    fun getHistoryTracks() : List<Track>

    fun clearTracksHistory()

}