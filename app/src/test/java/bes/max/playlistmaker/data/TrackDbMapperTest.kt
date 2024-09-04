package bes.max.playlistmaker.data

import bes.max.playlistmaker.data.db.entities.TrackEntity
import bes.max.playlistmaker.data.mappers.TrackDbMapper
import bes.max.playlistmaker.domain.models.Track
import org.junit.Assert.assertEquals
import org.junit.Test


class TrackDbMapperTest {

    private val mapper = TrackDbMapper()

    @Test
    fun trackDbMapperTestReturnsCorrectTrack() {
        val expected = Track(
            0L,
            "trackName",
            "artistName",
            0L,
            "",
            "collectionName",
            "year",
            "rock",
            "Russia",
            "",
            "01:38",
            "track.bigCover",
            "year"
        )

        val trackEntity= TrackEntity(
            trackId = 0L,
            trackName = "trackName",
            artistName = "artistName",
            collectionName = "collectionName",
            primaryGenreName = "rock",
            country = "Russia",
            previewUrl = "",
            trackTime = "01:38",
            bigCover = "track.bigCover",
            year = "year",
            addingDate = 589L,
            trackTimeMillis = 100_000L,
            isFavorite = false
        )

        assertEquals(expected, mapper.map(trackEntity))
    }
    @Test
    fun trackDbMapperTestReturnsCorrectTrackEntity() {
        val track = Track(
            0L,
            "trackName",
            "artistName",
            0L,
            "",
            "collectionName",
            "year",
            "rock",
            "Russia",
            "",
            "01:38",
            "track.bigCover",
            "year"
        )

        val expected = TrackEntity(
            trackId = 0L,
            trackName = "trackName",
            artistName = "artistName",
            collectionName = "collectionName",
            primaryGenreName = "rock",
            country = "Russia",
            previewUrl = "",
            trackTime = "01:38",
            bigCover = "track.bigCover",
            year = "year",
            addingDate = System.currentTimeMillis(),
            trackTimeMillis = 100_000L,
            isFavorite = false
        )

        assertEquals(expected.trackTime, mapper.map(track).trackTime)
        assertEquals(expected.artistName, mapper.map(track).artistName)
    }
}