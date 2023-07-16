package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class PlayerInteractor (private val player: Player) {

    private val getCurrentPlayingTimeUseCase: GetCurrentPlayingTimeUseCase = GetCurrentPlayingTimeUseCase(player)
    private val pauseTrackUseCase: PauseTrackUseCase = PauseTrackUseCase(player)
    private val playTrackUseCase: PlayTrackUseCase = PlayTrackUseCase(player)
    private val releasePlayerUseCase: ReleasePlayerUseCase = ReleasePlayerUseCase(player)
    private val preparePlayerUseCase: PreparePlayerUseCase = PreparePlayerUseCase(player)

    val state = player.playerState

    fun preparePlayer(dataSourceUrl: String) {
        preparePlayerUseCase.execute(dataSourceUrl)
    }

    fun play() {
        playTrackUseCase.execute()
    }

    fun pause() {
        pauseTrackUseCase.execute()
    }

    fun release() {
        releasePlayerUseCase.execute()
    }

    fun getCurrentTime() =
        getCurrentPlayingTimeUseCase.execute()
}