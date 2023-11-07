package bes.max.playlistmaker.domain.mediateka.playlistdetails

import android.content.Context
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.common.ExternalNavigator
import bes.max.playlistmaker.domain.models.Playlist

class SharePlaylistUseCase(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) {

    fun execute(playlist: Playlist) {
        val playlistString = StringBuilder("")
        playlistString.append(playlist.name)
        playlistString.append(System.lineSeparator())
        playlistString.append(playlist.description)
        playlistString.append(System.lineSeparator())
        playlistString.append(
            context.resources.getQuantityString(
                R.plurals.tracks_number,
                playlist.tracksNumber,
                playlist.tracksNumber
            )
        )
        playlistString.append(System.lineSeparator())
        var number = 1
        playlist.tracks?.forEach { track ->
            playlistString.append("$number. ${track.artistName} - ${track.trackName} (${track.trackTime})")
            playlistString.append(System.lineSeparator())
            number++
        }
        externalNavigator.sharePlaylist(playlistString.toString().trim())
    }
}