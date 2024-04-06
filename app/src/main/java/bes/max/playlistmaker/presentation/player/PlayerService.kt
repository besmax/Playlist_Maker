package bes.max.playlistmaker.presentation.player

import bes.max.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerService {

    val playerState: StateFlow<PlayerState>

    val currentPosition: StateFlow<Int>

    fun playback()

    fun getCurrentTime(): String

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Player Service"
        const val SERVICE_NOTIFICATION_ID = 101
        const val NOTIFICATION_CHANNEL_NAME = "Media player notification"
        const val EXTRA_ARTIST = "EXTRA_ARTIST"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_URL = "EXTRA_URL"
    }
}