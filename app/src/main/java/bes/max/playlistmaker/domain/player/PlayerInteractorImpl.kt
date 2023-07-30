package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.api.Player

class PlayerInteractorImpl (private val player: Player) : PlayerInteractor {

    private val getCurrentPlayingTimeUseCase: GetCurrentPlayingTimeUseCase = GetCurrentPlayingTimeUseCase(player)
    private val pauseTrackUseCase: PauseTrackUseCase = PauseTrackUseCase(player)
    private val playTrackUseCase: PlayTrackUseCase = PlayTrackUseCase(player)
    private val releasePlayerUseCase: ReleasePlayerUseCase = ReleasePlayerUseCase(player)
    private val preparePlayerUseCase: PreparePlayerUseCase = PreparePlayerUseCase(player)

    override val state = player.playerState

    override fun preparePlayer(dataSourceUrl: String) {
        preparePlayerUseCase.execute(dataSourceUrl)
    }

    override fun play() {
        playTrackUseCase.execute()
    }

    override fun pause() {
        pauseTrackUseCase.execute()
    }

    override fun release() {
        releasePlayerUseCase.execute()
    }

    override fun getCurrentTime() =
        getCurrentPlayingTimeUseCase.execute()
}