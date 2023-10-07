package bes.max.playlistmaker.data.mappers

import android.os.SystemClock
import bes.max.playlistmaker.data.db.TrackEntity
import bes.max.playlistmaker.domain.models.Track
import java.time.LocalDateTime

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
            bigCover = track.bigCover,
            year = track.year,
            addingDate = System.currentTimeMillis()
        )

    fun map(track: TrackEntity): Track =
        Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = 0L,
            artworkUrl100 = "",
            collectionName = track.collectionName,
            releaseDate = track.year,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackTime = track.trackTime,
            bigCover = track.bigCover,
            year = track.year
        )
}