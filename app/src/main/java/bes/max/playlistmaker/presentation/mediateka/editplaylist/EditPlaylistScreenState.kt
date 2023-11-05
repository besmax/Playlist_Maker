package bes.max.playlistmaker.presentation.mediateka.editplaylist

import bes.max.playlistmaker.domain.models.Playlist

sealed interface EditPlaylistScreenState {
    data class Editing(
        val playlist: Playlist
    ): EditPlaylistScreenState

    data object Updated: EditPlaylistScreenState
}