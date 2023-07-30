package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerInteractor {

    val state : StateFlow<PlayerState>
    fun preparePlayer(dataSourceUrl: String)

    fun play()

    fun pause()

    fun release()

    fun getCurrentTime() : Int

}