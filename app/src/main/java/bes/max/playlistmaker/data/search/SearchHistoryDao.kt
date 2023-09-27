package bes.max.playlistmaker.data.search

import bes.max.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDao {

    fun saveTrack(track: Track)

    fun getHistoryTracks() : Flow<List<Track>>

    fun clearTracksHistory()

}