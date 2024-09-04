package bes.max.playlistmaker.presentation.mediateka.playlistdetails

sealed interface PlaylistDetailsScreenState {

    object Default : PlaylistDetailsScreenState

    object Editing : PlaylistDetailsScreenState

    data class Content(val playlistDetails: PlaylistDetails) : PlaylistDetailsScreenState

    object Empty : PlaylistDetailsScreenState

}