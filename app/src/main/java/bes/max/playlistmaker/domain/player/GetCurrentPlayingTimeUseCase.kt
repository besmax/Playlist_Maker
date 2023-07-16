package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class GetCurrentPlayingTimeUseCase(private val player: Player) {

    fun execute() : Int = player.getCurrentPosition()
}