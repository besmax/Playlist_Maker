package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class PreparePlayerUseCase(private val player: Player) {

    fun execute(dataSourceUrl: String) {
        player.preparePlayer(dataSourceUrl)
    }
}