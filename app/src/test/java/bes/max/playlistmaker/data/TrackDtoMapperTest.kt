package bes.max.playlistmaker.data

import bes.max.playlistmaker.data.dto.TrackDto
import bes.max.playlistmaker.data.mappers.TrackDtoMapper
import bes.max.playlistmaker.domain.models.Track
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackDtoMapperTest {

    private val trackDtoMapperForTest = TrackDtoMapper()

    @Test
    fun trackDtoToTrackReturnsCorrectTrack() {
        val trackDto = TrackDto(
            0L,
            "trackName",
            "artistName",
            98000L,
            "blablabla/128x128.jpg",
            "",
            "2002.12.02",
            "rock",
            "Russia",
            ""
        )

        val expected = Track(
            0L,
            "trackName",
            "artistName",
            98000L,
            "blablabla/128x128.jpg",
            "",
            "2002.12.02",
            "rock",
            "Russia",
            "",
            "01:38",
            "blablabla/512x512bb.jpg",
            "2002"
        )

        assertEquals(expected, trackDtoMapperForTest.trackDtoToTrack(trackDto))
    }
}