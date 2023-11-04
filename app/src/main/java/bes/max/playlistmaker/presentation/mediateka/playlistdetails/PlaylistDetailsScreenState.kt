package bes.max.playlistmaker.presentation.mediateka.playlistdetails

import bes.max.playlistmaker.domain.models.Playlist

sealed interface PlaylistDetailsScreenState {

    object Default : PlaylistDetailsScreenState

    object Editing : PlaylistDetailsScreenState

    data class Content(val playlistDetails: PlaylistDetails) : PlaylistDetailsScreenState

    object Empty : PlaylistDetailsScreenState

}