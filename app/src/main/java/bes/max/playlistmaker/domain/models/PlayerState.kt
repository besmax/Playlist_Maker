package bes.max.playlistmaker.domain.models

sealed interface PlayerState {
    object STATE_DEFAULT : PlayerState
    object STATE_PREPARED : PlayerState
    object STATE_PLAYING : PlayerState
    object STATE_PAUSED : PlayerState
}