package bes.max.playlistmaker.presentation.player

import bes.max.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerService {

    val playerState: StateFlow<PlayerState>

    fun play()

    fun pause()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Player Service"
        const val SERVICE_NOTIFICATION_ID = 101
        const val NOTIFICATION_CHANNEL_NAME = "Media player notification"
        const val INTENT_FILTER_PAUSE_ACTION = "INTENT_FILTER_PAUSE_ACTION"
        const val ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND"
        const val ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND"
        const val EXTRA_ARTIST = "EXTRA_ARTIST"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_URL = "EXTRA_URL"
    }
}