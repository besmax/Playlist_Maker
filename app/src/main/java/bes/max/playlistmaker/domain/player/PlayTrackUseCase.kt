package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class PlayTrackUseCase(private val player: Player) {

    fun execute() {
        player.startPlayer()
    }
}