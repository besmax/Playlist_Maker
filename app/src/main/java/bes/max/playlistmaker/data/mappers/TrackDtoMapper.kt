package bes.max.playlistmaker.data.mappers

import bes.max.playlistmaker.data.dto.TrackDto
import bes.max.playlistmaker.domain.models.Track

class TrackDtoMapper {

    fun trackDtoToTrack(trackDto: TrackDto) : Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }
}