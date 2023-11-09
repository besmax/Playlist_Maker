package bes.max.playlistmaker.data.mappers

import bes.max.playlistmaker.data.db.entities.TrackEntity
import bes.max.playlistmaker.domain.models.Track

class TrackDbMapper {

    fun map(track: Track): TrackEntity =
        TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl ?: "",
            trackTime = track.trackTime,
            trackTimeMillis = track.trackTimeMillis,
            bigCover = track.bigCover,
            year = track.year,
            addingDate = System.currentTimeMillis(),
            isFavorite = track.isFavorite
        )

    fun map(track: TrackEntity): Track =
        Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = "",
            collectionName = track.collectionName,
            releaseDate = track.year,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackTime = track.trackTime,
            bigCover = track.bigCover,
            year = track.year,
            isFavorite = track.isFavorite ?: false
        )
}