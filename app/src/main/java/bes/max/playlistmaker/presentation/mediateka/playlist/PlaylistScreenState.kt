package bes.max.playlistmaker.presentation.mediateka.playlist

import bes.max.playlistmaker.domain.models.Playlist

sealed interface PlaylistScreenState {
    object Loading : PlaylistScreenState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistScreenState

    object Empty : PlaylistScreenState
}