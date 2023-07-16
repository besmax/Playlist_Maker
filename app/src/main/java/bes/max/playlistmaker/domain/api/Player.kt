package bes.max.playlistmaker.domain.api

import androidx.lifecycle.LiveData

interface Player {

    val playerState: LiveData<PlayerState>

    fun preparePlayer(dataSourceUrl: String)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPosition(): Int

    sealed interface PlayerState {
        object STATE_DEFAULT : PlayerState
        object STATE_PREPARED : PlayerState
        object STATE_PLAYING : PlayerState
        object STATE_PAUSED : PlayerState
    }

}