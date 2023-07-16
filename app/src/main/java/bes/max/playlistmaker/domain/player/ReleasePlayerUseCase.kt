package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class ReleasePlayerUseCase(private val player: Player) {

    fun execute() {
        player.releasePlayer()
    }
}