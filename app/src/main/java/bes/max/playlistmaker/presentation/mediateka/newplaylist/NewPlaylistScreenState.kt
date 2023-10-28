package bes.max.playlistmaker.presentation.mediateka.newplaylist

sealed interface NewPlaylistScreenState {

    object Default : NewPlaylistScreenState

    object Creating : NewPlaylistScreenState

    object Created : NewPlaylistScreenState

}