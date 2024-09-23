package bes.max.playlistmaker.presentation.mediateka.playlistdetails

interface PlaylistDetailsEvent {

    object OpenBottomSheetMenu: PlaylistDetailsEvent

    object CloseBottomSheetMenu: PlaylistDetailsEvent
}